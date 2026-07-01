# Multi-Source Federation Design Spec

**Goal:** Enable sndstrm to connect to multiple content sources simultaneously — personal Jellyfin servers, community feeds, and sndstrm.tv public content — presenting a unified home screen with intelligent source selection for playback.

**Core principle:** The app works out of the box with no server setup. sndstrm.tv public feed loads on first launch. Adding personal Jellyfin servers is optional.

---

## 1. Content Source Abstraction

All content providers implement a common `ContentSource` interface:

```
ContentSource
├── FeedContentSource      (sndstrm.tv, any JSON feed URL)
├── JellyfinContentSource  (personal Jellyfin servers)
└── (future: plugin sources)
```

### ContentSource Interface

```kotlin
interface ContentSource {
    val id: String
    val name: String
    val type: SourceType  // FEED or SERVER
    val state: StateFlow<ConnectionState>  // CONNECTED, DISCONNECTED, CONNECTING

    fun getHome(): Flow<List<ContentRow>>
    fun search(query: String): Flow<List<ContentItem>>
    suspend fun getItem(id: String): ContentItem?
    fun getStreamSources(item: ContentItem): List<StreamSource>
}
```

### ContentItem

Unified data class wrapping either a Jellyfin `BaseItemDto` or a feed item:

- `id: String`
- `title: String`
- `year: Int?`
- `overview: String?`
- `type: ContentType` (MOVIE, SERIES, EPISODE, LIVE, ANNOUNCEMENT)
- `images: ContentImages` (poster, backdrop, logo)
- `providerIds: Map<String, String>` (tmdb, imdb, tvdb)
- `streams: List<StreamSource>`
- `sourceId: String` (which ContentSource this came from)

Wraps `BaseItemDto` for Jellyfin sources — does not replace it internally.

### StreamSource

One playable version of a content item:

- `url: String`
- `codec: String` (h264, hevc, av1, etc.)
- `audioCodec: String` (aac, ac3, eac3, etc.)
- `container: String` (mp4, mkv, hls, etc.)
- `resolution: String` (480p, 720p, 1080p, 4k)
- `bitrate: Int` (kbps)
- `hash: String?` (sha256 content hash for deduplication)
- `sourceId: String`
- `sourceName: String`
- `canDirectPlay: Boolean` (computed against device capabilities)
- `isLive: Boolean`

---

## 2. Source Registry & Aggregation

### SourceRegistry

Manages all active content sources. Koin singleton.

```kotlin
interface SourceRegistry {
    val sources: StateFlow<List<ContentSource>>
    fun addSource(source: ContentSource)
    fun removeSource(id: String)
    fun getAggregatedHome(): Flow<List<ContentRow>>
    fun searchAll(query: String): Flow<List<ContentItem>>
}
```

### Aggregation Rules

- Each source provides `ContentRow` objects (continue watching, recently added, etc.)
- Rows interleaved by priority: primary server → secondary servers → feeds
- Source badge (small icon) on each card identifies origin
- Disconnected sources omitted silently — no error UI clutter

### Content Deduplication (ContentMatcher)

When the same title exists on multiple sources, group into a single `ContentItem` with multiple `StreamSource` entries.

**Matching strategy (configurable, default: provider-first):**
1. Match on TMDb/IMDb/TVDb IDs (exact)
2. Fallback: fuzzy title + year matching (Levenshtein distance ≤ 2, year ±1)

**Behavior:**
- Home screen shows one card per unique title
- Source badge shows "2+" indicator for multi-source items
- Deduplication runs on background coroutine with debounced cache

---

## 3. Source Selection at Playback

### SourceSelector

When playing an item with multiple `StreamSource` entries, selects the best one.

**Priority modes (user-configurable in Settings → Playback → Source Priority):**

| Mode | Strategy |
|---|---|
| Direct Play (default) | Prefer source device can decode natively |
| Quality | Prefer highest resolution/bitrate |
| Locality | Prefer lowest-latency server (measured via ping) |
| Manual | Show picker dialog listing all versions |

### Selection Flow

1. User presses Play on a `ContentItem`
2. `SourceSelector.select(item, mode)` evaluates all `StreamSource` entries
3. Score each: `directPlayScore + qualityScore + localityScore`, weighted by mode
4. If top source fails (server error, buffering timeout), auto-fallback to next-best
5. Fallback is transparent — brief "switching source" indicator

### Device Capability Detection

- Reuse existing `HdrHelper` and `deviceProfile.kt` for codec/container support
- Cache capability report per session
- Compare against each `StreamSource` to determine direct play feasibility

### UI

- Item detail: subtle chip "Available on 3 sources" when multiple exist
- Long-press Play: opens manual source picker regardless of mode
- Picker shows: server name, resolution, codec, direct play / transcode status

---

## 4. sndstrm.tv Feed Protocol

Lightweight JSON API served from a configurable base URL (default `https://sndstrm.tv`).

### Endpoints

| Endpoint | Purpose |
|---|---|
| `GET /feed/manifest` | Feed metadata — name, icon, version, capabilities |
| `GET /feed` | Home content rows (paginated, filterable by type) |
| `GET /feed/item/{id}` | Full item detail with stream URLs |
| `GET /feed/search?q=` | Search within feed content |

### Manifest

```json
{
  "name": "sndstrm.tv",
  "version": "1.0",
  "icon": "https://sndstrm.tv/icon.png",
  "description": "Free curated content for sndstrm",
  "capabilities": ["movies", "live", "channels", "announcements"],
  "auth_required": false,
  "sync_available": true,
  "sync_endpoint": "https://sndstrm.tv/sync",
  "updated_at": "2026-07-01T00:00:00Z"
}
```

### Feed Response

```json
{
  "rows": [
    {
      "id": "weekly-picks",
      "title": "Weekly Picks",
      "type": "collection",
      "updated_at": "2026-06-30T12:00:00Z",
      "items": [
        {
          "id": "pd-nightliving",
          "title": "Night of the Living Dead",
          "year": 1968,
          "type": "movie",
          "runtime_minutes": 96,
          "overview": "...",
          "genres": ["Horror"],
          "rating": 7.2,
          "images": {
            "poster": "https://sndstrm.tv/images/poster.jpg",
            "backdrop": "https://sndstrm.tv/images/backdrop.jpg"
          },
          "provider_ids": { "tmdb": "10331", "imdb": "tt0063350" },
          "streams": [
            {
              "url": "https://archive.org/download/.../film.mp4",
              "codec": "h264",
              "audio_codec": "aac",
              "container": "mp4",
              "resolution": "1080p",
              "bitrate_kbps": 4500,
              "hash": "sha256:e3b0c44298fc1c149afbf4c8996fb924...",
              "source_name": "Archive.org"
            }
          ]
        }
      ]
    },
    {
      "id": "live-community",
      "title": "Community Streams",
      "type": "live",
      "items": [...]
    },
    {
      "id": "sndstrm-news",
      "title": "sndstrm News",
      "type": "announcement",
      "items": [...]
    }
  ],
  "pagination": { "page": 1, "total_pages": 3, "total_items": 47 }
}
```

### Server-Side Architecture

The feed server is a static site generator, not a runtime application:

- Curation happens in a git repo via YAML files
- `build-feed.py` resolves items from sources (archive.org, etc.), enriches with TMDb metadata, generates JSON
- `validate-streams.py` runs in CI to verify stream URLs
- Output deployed to CDN (Cloudflare Pages, S3, etc.)
- No database, no runtime, minimal operational burden

### Feed URL Configuration

- Default: `https://sndstrm.tv`
- Configurable in Settings → Content Sources → Feed URL
- Anyone can run their own feed server and point sndstrm at it

### Client Caching

- Feed responses cached with ETag/Last-Modified (1-hour default TTL)
- Manifest cached 24 hours
- Images through existing Coil cache

---

## 5. Watch State & Privacy Tiers

### Tier A: Isolated (default, free)

- Each Jellyfin server tracks its own watch state via Jellyfin API
- Feed content has no watch tracking
- "Continue watching" only shows items from servers that report progress
- No data leaves the device beyond normal Jellyfin communication

### Tier B: Local Merge (free)

- Local SQLite database tracks unified watch state across all sources
- Schema: `(provider_id, source_id) → progress_ticks, watched, last_played`
- "Continue watching" aggregates from Jellyfin server state AND local DB
- Feed items get resume support via local DB
- Data never leaves the device
- Survives app restarts, not app reinstalls

### Tier C: Cloud Sync (sndstrm.tv account, paid tier)

- User creates sndstrm.tv account (email + password or OAuth)
- Watch state syncs to `POST /sync/state` on sndstrm.tv
- Encrypted at rest — sndstrm.tv stores blobs it can't read without user's key
- Enables cross-device resume, new device setup
- Auth: bearer token in auth store alongside Jellyfin credentials
- Eventually consistent — local DB is source of truth, cloud is backup
- Provides legitimate infrastructure subsidization model

### Settings UI

Settings → Privacy → Watch History:
- "Per-server only" (Tier A)
- "Unified on this device" (Tier B)
- "Sync across devices" (Tier C) — prompts account creation

---

## 6. Content-Addressed Caching

### Content Hashing

Stream entries include a `hash` field (sha256) identifying the file content:

```json
{
  "url": "https://archive.org/download/.../film.mp4",
  "hash": "sha256:e3b0c44298fc1c149afbf4c8996fb924..."
}
```

### ContentCache

- Backed by Android external files directory
- Keyed by content hash, not URL
- Stores partial and complete media files
- Before streaming any URL: `ContentCache.has(hash)` → play locally if present

### Deduplication Payoff

- User watches a film from Server A → cached by hash
- Same film on sndstrm.tv feed with same hash → plays from cache, zero network
- Different encode → different hash → no false match

### Cache Management (Settings → Storage → Content Cache)

- Size limit: configurable (default 2GB)
- LRU eviction (least recently played first)
- "Clear content cache" with size display
- Per-item: long-press → "Remove from cache"

### Future: BitTorrent Seeding Layer (Phase 5)

- For cached content with associated `.torrent` file, optionally seed on Wi-Fi
- Toggle: "Help distribute public content" (off by default)
- Leverages archive.org's existing torrent infrastructure
- Content hash links HTTP stream and torrent — same file, two delivery paths

---

## 7. Implementation Phases

Each phase is independently shippable and testable.

### Phase 1: Feed Integration (MVP)

- `ContentSource` interface + `FeedContentSource`
- `SourceRegistry` with single feed source
- sndstrm.tv feed pre-configured on first launch
- Home screen renders feed rows alongside Jellyfin rows
- No deduplication, no source selection — parallel display
- Configurable feed URL in settings
- **Result:** App works out of the box without a Jellyfin server

### Phase 2: Multi-Server

- `JellyfinContentSource` wrapping existing Jellyfin SDK
- Support 2+ simultaneous Jellyfin server sessions
- Refactor `SessionRepository` from single to multi-session
- Source badges on cards, home screen interleaving
- **Result:** Browse multiple Jellyfin libraries in one UI

### Phase 3: Deduplication & Source Selection

- `ContentMatcher` (provider ID + fuzzy fallback)
- `SourceSelector` with configurable priority modes
- Auto-fallback on playback failure, manual picker on long-press
- Device capability scoring
- **Result:** Smart playback from best available source

### Phase 4: Watch State Sync

- Local SQLite unified watch DB (Tier B)
- sndstrm.tv account system + encrypted cloud sync (Tier C)
- Cross-device resume
- **Result:** Seamless experience across devices

### Phase 5: Content-Addressed Caching & P2P

- Content hash integration in feed protocol and Jellyfin source
- Local content cache with hash-based deduplication
- Optional BitTorrent seeding for cached public content
- **Result:** Reduced bandwidth, offline playback, community distribution

---

## Non-Goals (v1)

- Server-side transcoding management (that's Jellyfin's job)
- DRM content (feed content is public domain / freely licensed)
- Social features beyond cloud sync (no chat, no comments, no ratings)
- Plugin API for third-party sources (architecture supports it, API not exposed yet)
