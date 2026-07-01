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

- `canonicalId: String` — stable ID for deduped items, derived from best available provider ID (`tmdb:10331`) or `{sourceId}:{itemId}` for providerless items
- `title: String`
- `year: Int?`
- `overview: String?`
- `type: ContentType` (MOVIE, SERIES, EPISODE, LIVE, ANNOUNCEMENT)
- `images: ContentImages` (poster, backdrop, logo)
- `providerIds: Map<String, String>` (tmdb, imdb, tvdb)
- `sourceRefs: List<SourceRef>` — one entry per source that has this item
- `streams: List<StreamSource>` — aggregated from all sourceRefs

A `ContentItem` does NOT have a single `sourceId`. After deduplication, it may represent the same title from multiple sources. The `sourceRefs` list tracks each origin.

### SourceRef

Per-source reference to an item:

- `sourceId: String` — which ContentSource
- `itemId: String` — the item's ID within that source (Jellyfin UUID or feed item ID)
- `baseItem: BaseItemDto?` — the original Jellyfin DTO, if this ref is from a Jellyfin source (null for feed sources). Preserved for Jellyfin-specific operations (playback sessions, progress reporting, subtitle/audio stream selection).

### StreamSource

One playable version of a content item:

- `sourceRef: SourceRef` — which source and item this stream belongs to
- `codec: String` (h264, hevc, av1, etc.)
- `audioCodec: String` (aac, ac3, eac3, etc.)
- `container: String` (mp4, mkv, hls, etc.)
- `resolution: String` (480p, 720p, 1080p, 4k)
- `bitrate: Int` (kbps)
- `hash: String?` (sha256 content hash, feed sources only — see Section 6 for limitations)
- `canDirectPlay: Boolean` (computed against device capabilities)
- `isLive: Boolean`

### PlayableResolver

Source-specific contract for turning a `StreamSource` into an actual playback session. Different sources have fundamentally different playback requirements:

```kotlin
interface PlayableResolver {
    suspend fun resolve(streamSource: StreamSource): PlaybackSession
}
```

**`JellyfinPlayableResolver`:** Uses the `SourceRef.baseItem` (BaseItemDto) to:
- Create a play session via Jellyfin API (`/Sessions/Playing`)
- Select media source and determine transcoding needs
- Bind subtitle/audio stream selection
- Get the actual stream URL (which may be a transcoding URL, not a direct file)
- Report progress back to the correct server's API client
- Handle play session lifecycle (start, progress, stop)

**`FeedPlayableResolver`:** Simply returns the stream URL directly. No session management, no progress reporting, no transcoding negotiation.

Each `ContentSource` provides its own `PlayableResolver` implementation. `SourceSelector` picks the best `StreamSource`, then delegates to the corresponding resolver.

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

### Multi-Server Session Architecture

The current codebase assumes one active `Session`, one bound `ApiClient`, and one `UserSettingPreferences` store. Multi-server requires:

**Per-server API clients:** Each `JellyfinContentSource` owns its own `ApiClient` instance, authenticated independently. These are NOT registered as Koin singletons — the `SourceRegistry` holds them. The existing global `ApiClient` Koin binding becomes the "primary server" client for backward compatibility during migration.

**Per-server auth state:** Each `JellyfinContentSource` stores:
- `serverId: UUID`
- `userId: UUID`
- `accessToken: String`
- `apiClient: ApiClient` (configured for that server's URL + token)

**Session lifecycle:** `SessionRepository` retains its current single-session API for the primary server. Secondary servers connect/disconnect independently via `SourceRegistry.addSource()` / `removeSource()`. No global "switch session" when interacting with a secondary server's content — the `SourceRef.sourceId` routes API calls to the correct client.

**Telemetry/crash reporting:** Bound to the primary server only. Secondary servers do not receive telemetry from the client.

**`UserSettingPreferences` scoping:** Server-synced preferences (display preferences, home sections) are per-server. Only the primary server's preferences drive the home layout. Secondary servers contribute content rows but do not override layout preferences.

### Aggregation Rules

- Each source provides `ContentRow` objects (continue watching, recently added, etc.)
- Rows interleaved by priority: primary server → secondary servers → feeds
- Source badge (small icon) on each card identifies origin
- Disconnected sources omitted silently — no error UI clutter
- Deduplication applies to global surfaces (search, "Recently Added") but NOT to source-owned rows ("Continue Watching" from Server A stays distinct from Server B's)

### Content Deduplication (ContentMatcher)

When the same title exists on multiple sources, group into a single `ContentItem` with multiple `SourceRef` and `StreamSource` entries.

**Matching strategy (configurable, default: provider-first):**
1. Match on TMDb/IMDb/TVDb IDs (exact)
2. Fallback: fuzzy title + year matching (Levenshtein distance ≤ 2, year ±1)
3. No match: items remain separate (no false deduplication)

**Behavior:**
- Aggregated surfaces show one card per unique title with "2+" source badge
- Source-owned rows (continue watching, next up) are never deduped — they show per-server state
- Deduplication runs on background coroutine with debounced cache
- `ContentItem.canonicalId` is the dedup key — derived from best provider ID

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

**Security constraints for feed URLs:**
- HTTPS required — HTTP URLs rejected with a clear error message
- URL must resolve to a valid manifest (`/feed/manifest`) before being accepted
- Stream URLs within feed responses are validated: only `https://` and `http://` schemes allowed (no `file://`, `content://`, `javascript:`, etc.)
- Feed responses > 10MB rejected (prevent memory exhaustion)
- Image URLs go through Coil's existing security model

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

Local SQLite database tracks unified watch state across all sources.

**Schema:**
```sql
CREATE TABLE watch_state (
    canonical_id TEXT NOT NULL,  -- ContentItem.canonicalId (e.g., "tmdb:10331")
    source_id TEXT,              -- NULL for unified entries, source ID for source-specific
    item_id TEXT,                -- source-specific item ID for back-mapping
    progress_ticks INTEGER DEFAULT 0,
    watched INTEGER DEFAULT 0,
    last_played INTEGER,         -- epoch millis
    PRIMARY KEY (canonical_id, source_id)
);
```

**Key design decisions:**
- Keyed by `canonical_id` (provider-derived) for cross-source unification
- `source_id` is NULL for the unified row, non-NULL for source-specific overrides
- Items without provider IDs use `{sourceId}:{itemId}` as canonical_id — these cannot deduplicate but still participate in local tracking
- Episodes keyed as `tmdb:series:10331:S02E05` (series provider ID + season/episode)
- When Jellyfin reports progress AND local DB has progress, Jellyfin wins (it's the authoritative source for its own content). Local DB fills in for feed items and cross-source resume.
- "Continue watching" row: query Jellyfin servers for their resume items UNION local DB for feed resume items, dedup by canonical_id, sort by last_played

**Data never leaves the device. Survives app restarts, not app reinstalls.**

### Tier C: Cloud Sync (sndstrm.tv account, paid tier)

- User creates sndstrm.tv account (OAuth only — no password management burden)
- Watch state syncs to `POST /sync/state` on sndstrm.tv
- Auth: OAuth bearer token stored in auth store alongside Jellyfin credentials

**Encryption model:**
- On account creation, client generates a 256-bit AES key derived from a user-chosen passphrase via Argon2id
- Key never leaves the device — sndstrm.tv never sees it
- Sync payloads are AES-GCM encrypted client-side before upload
- New device setup: user enters passphrase → derives same key → decrypts sync blob
- Passphrase loss = sync data loss (no recovery). Local DB remains intact.
- sndstrm.tv stores: encrypted blob, last sync timestamp, blob size. No plaintext metadata.

**Limitation:** OAuth token identifies the user for blob storage routing, but sndstrm.tv cannot inspect blob contents. This is a genuine zero-knowledge design — the trade-off is no server-side features on the watch data (no recommendations, no trending).

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

Stream entries from feed sources include a `hash` field (sha256) computed at build time by the feed server:

```json
{
  "url": "https://archive.org/download/.../film.mp4",
  "hash": "sha256:e3b0c44298fc1c149afbf4c8996fb924..."
}
```

**Hash availability by source type:**

| Source | Hash available? | How |
|---|---|---|
| Feed (sndstrm.tv) | Yes | Computed by `build-feed.py` at feed generation time |
| Jellyfin server | No | Jellyfin API does not expose full-file SHA-256. Client-side hashing requires downloading the full file first, which defeats the purpose. |

**Implication:** Content-addressed deduplication works between feed sources and between feed + local cache. It does NOT work across two Jellyfin servers — those rely on metadata matching (provider IDs) for deduplication, not file hashing. This is acceptable because Jellyfin-to-Jellyfin dedup is handled by `ContentMatcher` (Section 2), not by file identity.

### ContentCache

- Backed by Android external files directory
- Dual-keyed: by content hash (when available) AND by URL (fallback)
- Tracks file state: `COMPLETE`, `PARTIAL(bytes_downloaded, total_bytes)`, `MISSING`
- `ContentCache.has(hash)` returns `CacheHit.COMPLETE`, `CacheHit.PARTIAL(path, range)`, or `CacheHit.MISS`
- Complete hits: play from local file
- Partial hits: resume download via HTTP Range header from last byte, play when sufficient buffer available
- Feed items with hashes: cached by hash (cross-URL dedup)
- Jellyfin items and hashless feed items: cached by URL (no cross-source dedup, but still avoids re-download)

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

- `ContentSource` interface + `FeedContentSource` + `FeedPlayableResolver`
- `SourceRegistry` with single feed source
- sndstrm.tv feed pre-configured on first launch
- Home screen renders feed rows alongside Jellyfin rows (if a server is configured)
- Feed items are fully playable through the existing ExoPlayer/Media3 video player — not browse-only
- No deduplication, no source selection — each source's content displayed separately
- Configurable feed URL in settings (HTTPS required)
- **Result:** App works out of the box without a Jellyfin server. Users can browse and play public content immediately.

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
