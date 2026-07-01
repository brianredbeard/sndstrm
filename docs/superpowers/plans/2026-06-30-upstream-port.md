# sndstrm Upstream Port Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Port DUNE's feature enhancements to a clean upstream Jellyfin Android TV v0.19.9 base, producing a buildable, testable sndstrm release that tracks upstream.

**Architecture:** The `upstream-port` branch starts from a clean `v0.19.9` tag. Each task creates a feature branch (`port/<feature>`), implements one DUNE feature adapted to v0.19.9's APIs, and merges back. The `master` branch (current DUNE-based sndstrm) remains as a stable fallback. Features are ported in dependency order — branding first (everything depends on it), then infrastructure (preferences, DI), then UI features.

**Tech Stack:** Kotlin, Jetpack Compose, Android TV Leanback, Koin DI, Jellyfin SDK, ExoPlayer/Media3

## Global Constraints

- Base: upstream jellyfin-androidtv `v0.19.9` (tag `14a5e160e`)
- Branch: `upstream-port` (already created)
- Feature branches: `port/<feature-name>` off `upstream-port`
- Build: `./gradlew assembleStandardDebug` must pass after every task merge
- JDK: 21 (Temurin)
- Package ID: `tv.sndstrm.app` (standard), `tv.sndstrm.enhanced` (enhanced)
- Copyright headers on all new/modified files (dual Jellyfin + sndstrm attribution)
- Upstream `UserSettingPreferences` now extends `DisplayPreferencesStore(api)` not `SharedPreferenceStore(context)` — constructor takes `ApiClient`, not `Context`
- Upstream `UserPreferences` companion uses `AppTheme.DARK` default, not `AppTheme.MUTED_PURPLE`
- Upstream preference screens use `dsl.OptionsFragment` pattern — DUNE's Compose-migrated screens need to be re-ported using this pattern or upstream's own Compose equivalents if present
- Upstream `PlaybackModule` calls `jellyfinPlugin(api, deviceProfileBuilder, lifecycle)` (3 args) and `R.drawable.app_icon_foreground`

## Branch Structure

```
v0.19.9 (upstream tag)
  └── upstream-port (integration branch)
       ├── port/01-branding          ← app identity, build config, GPL headers
       ├── port/02-themes            ← OLED/dark themes, color schemes, attrs
       ├── port/03-home-screen       ← custom sections, combined rows, carousel
       ├── port/04-playback          ← overlay enhancements, HW accel, audio
       ├── port/05-subtitles         ← download, TX3G, language defaults
       ├── port/06-settings          ← cache config, genre manager, language selector
       ├── port/07-login-ui          ← profile views, avatars, server screen
       ├── port/08-theme-songs       ← theme song playback, archive fallback
       ├── port/09-search            ← search UI enhancements
       └── port/10-polish            ← bug fixes, DreamView, misc improvements
```

---

### Task 1: Branding & GPL-2.0 Compliance

**Files:**
- Modify: `app/build.gradle.kts` (applicationId, APK naming, flavors)
- Modify: `app/src/main/res/values/strings.xml` (app name strings)
- Create: `FORK.md`
- Modify: `NOTICE`
- Modify: `README.md`
- Modify: `CONTRIBUTING.md`
- Modify: All `.kt`/`.java` files (copyright headers via script)

**Interfaces:**
- Consumes: Clean v0.19.9 checkout
- Produces: `tv.sndstrm.app` package ID, `sndstrm` app name, GPL-2.0 headers on all files

**Reference:** The branding work is already done on `master` — this task reapplies it to the v0.19.9 base. Copy the following files verbatim from `master`: `FORK.md`, `NOTICE`, `README.md`, `CONTRIBUTING.md`.

- [ ] **Step 1: Create feature branch**

```bash
git checkout upstream-port
git checkout -b port/01-branding
```

- [ ] **Step 2: Apply build config changes**

Copy `app/build.gradle.kts` from `master` branch, then reconcile with upstream's v0.19.9 version:
- Keep upstream's `signingConfigs`, `dependenciesInfo`, and dependency declarations
- Apply sndstrm's `applicationId = "tv.sndstrm.app"`, enhanced flavor, APK naming

```bash
# Start from upstream's build.gradle.kts, then apply sndstrm changes
git show v0.19.9:app/build.gradle.kts > app/build.gradle.kts
```

Then edit to change:
- `applicationId` from `"org.jellyfin.androidtv"` to `"tv.sndstrm.app"`
- Add `enhanced` product flavor with `applicationId = "tv.sndstrm.enhanced"`
- Add APK output naming: `sndstrm-androidtv-${versionName}.apk`
- Add `app_id`, `app_search_suggest_authority`, `app_search_suggest_intent_data` string resources

- [ ] **Step 3: Apply string branding**

```bash
sed -i '' 's/>Jellyfin</>sndstrm</g; s/>Jellyfin Debug</>sndstrm Debug</g' app/src/main/res/values/strings.xml
```

- [ ] **Step 4: Copy documentation files from master**

```bash
git show master:FORK.md > FORK.md
git show master:NOTICE > NOTICE
git show master:README.md > README.md
git show master:CONTRIBUTING.md > CONTRIBUTING.md
```

- [ ] **Step 5: Add copyright headers to all source files**

Run the header script from master (see commit `a63afa829` for the script logic):
- Files present in v0.19.9: dual copyright (Jellyfin + sndstrm)
- New files added in this port: sndstrm-only copyright

- [ ] **Step 6: Update telemetry/updater URLs**

Modify these files to reference `brianredbeard/sndstrm`:
- `app/src/main/java/org/jellyfin/androidtv/util/profile/deviceProfileReport.kt`: client name + repo URL
- `app/src/main/java/org/jellyfin/androidtv/telemetry/TelemetryService.kt`: client name + repo URL

- [ ] **Step 7: Build and verify**

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
./gradlew clean assembleStandardDebug
```

Expected: BUILD SUCCESSFUL

- [ ] **Step 8: Commit and merge**

```bash
git add -A
git commit -m "port: sndstrm branding and GPL-2.0 compliance on v0.19.9 base"
git checkout upstream-port
git merge port/01-branding --no-ff -m "Merge port/01-branding: sndstrm identity on v0.19.9"
```

---

### Task 2: Theme System (OLED, Dark Purple, Emerald, etc.)

**Files:**
- Create: `app/src/main/res/values/theme_basic.xml`
- Create: `app/src/main/res/values/theme_darkpurple.xml`
- Create: `app/src/main/res/values/theme_emerald.xml`
- Create: `app/src/main/res/values/theme_flexy.xml`
- Create: `app/src/main/res/values/theme_mutedpurple.xml`
- Create: `app/src/main/res/values/theme_purplehaze.xml`
- Create: `app/src/main/res/values/theme_yellowtown.xml`
- Create: `app/src/main/res/values/colors_purplehaze.xml`
- Modify: `app/src/main/res/values/attrs.xml` (add `backdrop_fading_color` and other theme attrs)
- Modify: `app/src/main/res/values/colors.xml` (add DUNE color definitions)
- Create: `app/src/main/java/org/jellyfin/androidtv/preference/constant/AppTheme.kt` (extend upstream's enum with new themes)
- Modify: `app/src/main/java/org/jellyfin/androidtv/ui/base/colorScheme.kt` (wire new themes)

**Interfaces:**
- Consumes: Task 1 (branding complete, builds clean)
- Produces: `AppTheme` enum with MUTED_PURPLE, DARK_PURPLE, EMERALD, PURPLEHAZE, YELLOWTOWN, FLEXY, BASIC values; theme resources loadable via `setTheme()`

**Key API difference:** Upstream `AppTheme` enum has `DARK`, `EMERALD`. DUNE added `MUTED_PURPLE`, `DARK_PURPLE`, `PURPLEHAZE`, `YELLOWTOWN`, `FLEXY`, `BASIC`. The enum values map to Android style resources via `colorScheme.kt`.

- [ ] **Step 1: Create feature branch**

```bash
git checkout upstream-port
git checkout -b port/02-themes
```

- [ ] **Step 2: Copy theme resource files from master**

```bash
for f in theme_basic theme_darkpurple theme_emerald theme_flexy theme_mutedpurple theme_purplehaze theme_yellowtown colors_purplehaze; do
  git show master:app/src/main/res/values/${f}.xml > app/src/main/res/values/${f}.xml
done
```

- [ ] **Step 3: Merge attrs.xml additions**

Add DUNE's custom theme attributes (`backdrop_fading_color`, tile colors, focus border, etc.) to upstream's `attrs.xml`. Source: `git show master:app/src/main/res/values/attrs.xml`

- [ ] **Step 4: Merge color definitions into colors.xml**

Add DUNE's color definitions (dark_purple_*, flexy_*, muted_purple_*, emerald_*, yellowtown_*) to upstream's `colors.xml`. Avoid duplicating colors already defined upstream.

- [ ] **Step 5: Extend AppTheme enum**

Add new enum values to upstream's `AppTheme.kt`. Map each to its theme style resource.

- [ ] **Step 6: Wire themes in colorScheme.kt**

Add `when` branches for each new `AppTheme` value that return the corresponding color scheme.

- [ ] **Step 7: Build and verify**

```bash
./gradlew clean assembleStandardDebug
```

Expected: BUILD SUCCESSFUL. Theme enum compiles, resources link.

- [ ] **Step 8: Commit and merge**

```bash
git add -A
git commit -m "port: DUNE theme system (OLED, dark purple, emerald, purplehaze, yellowtown, flexy)"
git checkout upstream-port
git merge port/02-themes --no-ff
```

---

### Task 3: Home Screen Customization

**Files:**
- Create: `app/src/main/java/org/jellyfin/androidtv/ui/home/GenreManager.kt`
- Create: `app/src/main/java/org/jellyfin/androidtv/ui/home/HomeFragmentSuggestedMoviesFragmentRow.kt`
- Create: `app/src/main/java/org/jellyfin/androidtv/ui/home/HomeFragmentMusicVideosRow.kt`
- Create: `app/src/main/java/org/jellyfin/androidtv/ui/home/CombinedResumeNextUpAdapter.kt`
- Create: `app/src/main/java/org/jellyfin/androidtv/ui/home/carousel/` (CarouselItem, CarouselViewModel, FeaturedCarousel)
- Modify: `app/src/main/java/org/jellyfin/androidtv/ui/home/HomeFragment.kt`
- Modify: `app/src/main/java/org/jellyfin/androidtv/ui/home/HomeFragmentHelper.kt`
- Modify: `app/src/main/java/org/jellyfin/androidtv/ui/home/HomeRowsFragment.kt`
- Modify: `app/src/main/java/org/jellyfin/androidtv/preference/UserSettingPreferences.kt` (add genre row preferences)
- Modify: `app/src/main/java/org/jellyfin/androidtv/di/AppModule.kt` (register GenreManager)

**Interfaces:**
- Consumes: Task 2 (theme system — carousel uses theme colors)
- Produces: `GenreManager` (genre row loading), `FeaturedCarousel` (Compose carousel), `CombinedResumeNextUpAdapter`, "Because You Watched" row, music videos row

**Key API difference:** Upstream `UserSettingPreferences` extends `DisplayPreferencesStore(api: ApiClient)`, not `SharedPreferenceStore(context: Context)`. Genre row preferences must be added to the companion object as `booleanPreference(...)` calls. The `HomeFragment` uses upstream's fragment-based architecture — wire new rows through `HomeFragmentHelper.loadRows()`.

- [ ] **Step 1-8:** Same pattern as Task 2. Create branch, port files adapting to upstream APIs, build, verify, merge.

**Critical adaptation:** `GenreManager` constructor takes `UserSettingPreferences` — adapt to use upstream's API-backed preference store. Sort preferences (`genreSortBy`) must be added to `UserPreferences` companion.

---

### Task 4: Playback Enhancements

**Files:**
- Create: `app/src/main/java/org/jellyfin/androidtv/ui/playback/overlay/action/EpisodesAction.kt`
- Create: `app/src/main/java/org/jellyfin/androidtv/ui/playback/overlay/CustomSeekBar.java`
- Create: `app/src/main/java/org/jellyfin/androidtv/ui/playback/overlay/PlaybackGestureDetector.java`
- Create: `app/src/main/java/org/jellyfin/androidtv/ui/playback/PlaybackControllerExtensions.kt`
- Modify: `app/src/main/java/org/jellyfin/androidtv/ui/playback/PlaybackController.java`
- Modify: `app/src/main/java/org/jellyfin/androidtv/ui/playback/VideoManager.java`
- Modify: `app/src/main/java/org/jellyfin/androidtv/preference/UserPreferences.kt` (add HW accel toggle, audio downmix)

**Interfaces:**
- Consumes: Task 1 (branding)
- Produces: Episode selector overlay action, HW acceleration toggle, audio downmix auto-fallback, chapter overlay support, configurable skip duration

**Key API difference:** Upstream's `PlaybackController` and `VideoManager` have been modified since DUNE forked. Port changes carefully against v0.19.9 versions — diff DUNE's modifications against the fork point to isolate the delta, then apply to v0.19.9.

---

### Task 5: Subtitle Enhancements

**Files:**
- Create: `app/src/main/java/org/jellyfin/androidtv/ui/SubtitleManagementPopup.java`
- Modify: `app/src/main/java/org/jellyfin/androidtv/ui/playback/PlaybackController.java` (subtitle index handling)
- Modify: `app/src/main/java/org/jellyfin/androidtv/preference/UserPreferences.kt` (default subtitle language)
- Create: `app/src/main/res/layout/subtitle_management_popup.xml`
- Create: `app/src/main/res/layout/subtitle_preview.xml`

**Interfaces:**
- Consumes: Task 4 (playback controller modifications)
- Produces: Subtitle download UI, TX3G/MOV_TEXT support, default subtitle language preference

---

### Task 6: Settings & Preferences

**Files:**
- Create: `app/src/main/java/org/jellyfin/androidtv/ui/preference/screen/BackdropSettingsPreferencesScreen.kt`
- Create: `app/src/main/java/org/jellyfin/androidtv/ui/preference/screen/GenresPreferenceScreen.kt`
- Create: `app/src/main/java/org/jellyfin/androidtv/ui/preference/screen/ThemeSongPreferencesScreen.kt`
- Create: `app/src/main/java/org/jellyfin/androidtv/ui/preference/screen/DisplayPreferencesScreen.kt`
- Modify: `app/src/main/java/org/jellyfin/androidtv/ui/preference/screen/CustomizationPreferencesScreen.kt` (add links to new screens)
- Modify: `app/src/main/java/org/jellyfin/androidtv/preference/UserPreferences.kt` (cache size, image quality prefs)

**Interfaces:**
- Consumes: Tasks 2-5 (themes, home, playback, subtitles — preferences for all)
- Produces: Preference screens for backdrop settings, genre row config, theme songs, display preferences, cache management

**Key API difference:** Upstream uses `OptionsFragment`-based DSL for preference screens. DUNE migrated some screens to Compose. For this port, use upstream's `OptionsFragment` pattern to minimize conflict surface. Compose migration can happen in a future iteration.

---

### Task 7: Login UI Enhancements

**Files:**
- Create: `app/src/main/java/org/jellyfin/androidtv/ui/FocusableProfileView.kt`
- Create: `app/src/main/java/org/jellyfin/androidtv/ui/ProfileImageContainer.kt`
- Create: `app/src/main/res/drawable/tile_user.xml` (and related user profile drawables)
- Create: `app/src/main/res/layout/view_circular_user_profile.xml`
- Modify: `app/src/main/java/org/jellyfin/androidtv/ui/startup/StartupActivity.kt`
- Modify: `app/src/main/java/org/jellyfin/androidtv/ui/startup/fragment/SelectServerFragment.kt`
- Create: `app/src/main/assets/Default1.png`, `Default2.png`, `Default3.png` (default avatars)

**Interfaces:**
- Consumes: Task 2 (theme system — login uses theme colors)
- Produces: Redesigned server/user selection with default avatars, focus management, visual feedback

---

### Task 8: Theme Songs

**Files:**
- Create: `app/src/main/java/org/jellyfin/androidtv/ui/itemdetail/ThemeSongs.kt`
- Create: `app/src/main/java/org/jellyfin/androidtv/ui/itemdetail/archivehelper.kt`
- Modify: `app/src/main/java/org/jellyfin/androidtv/preference/UserPreferences.kt` (theme song prefs)
- Modify: `app/src/main/java/org/jellyfin/androidtv/ui/itemdetail/FullDetailsFragment.java`

**Interfaces:**
- Consumes: Task 1 (branding)
- Produces: Theme song playback on item detail screens with archive.org fallback, volume control, per-media-type toggles

---

### Task 9: Search UI Enhancements

**Files:**
- Create: `app/src/main/java/org/jellyfin/androidtv/ui/search/SearchScreen.kt`
- Modify: `app/src/main/java/org/jellyfin/androidtv/ui/search/SearchFragment.kt`
- Modify: `app/src/main/res/layout/fragment_search.xml`

**Interfaces:**
- Consumes: Task 2 (theme system)
- Produces: Enhanced search UI with voice input improvements, Compose integration

---

### Task 10: Polish & Miscellaneous

**Files:**
- Create: `app/src/main/java/org/jellyfin/androidtv/ui/ScreensaverViewModel.kt`
- Create: `app/src/main/java/org/jellyfin/androidtv/util/AppUpdater.kt` + `UpdateResult.kt`
- Create: `app/src/main/java/org/jellyfin/androidtv/ui/VerticalAlphaPickerView.kt`
- Modify: `app/src/main/java/org/jellyfin/androidtv/integration/dream/DreamViewModel.kt` (DreamView enhancements)
- Modify: `app/src/main/java/org/jellyfin/androidtv/ui/browsing/BrowseGridFragment.java` (grid direction, poster sizes)

**Interfaces:**
- Consumes: All prior tasks
- Produces: In-app updater (pointing to brianredbeard/sndstrm releases), DreamView screensaver with transitions, vertical alphabet picker for grid browsing, additional poster size options

---

## Execution Notes

### How to isolate DUNE's changes per feature

For each task, the implementer should:

```bash
# See what DUNE changed in a specific file vs the fork point
git diff upstream-fork-point master -- <file-path>

# See all DUNE-added files in a directory
git diff --name-only --diff-filter=A upstream-fork-point master -- <directory/>

# Get DUNE's version of a file
git show master:<file-path>
```

Then adapt those changes to work against upstream v0.19.9's APIs.

### Testing strategy

Each task must:
1. `./gradlew assembleStandardDebug` passes (compile check)
2. `./gradlew test` passes (unit tests)
3. Manual verification on Android TV emulator or device if touching UI

### Rollback

Each feature is a `--no-ff` merge into `upstream-port`. If a feature causes problems, it can be reverted with `git revert -m 1 <merge-commit>` without affecting other features.

### Parallel work

Tasks 4, 5, 7, 8, 9 are independent of each other (all depend only on Tasks 1-2). They can be worked on in parallel by different contributors.

```
Task 1 (branding) ──► Task 2 (themes) ──► Task 3 (home screen) ──► Task 6 (settings)
                                      ├──► Task 4 (playback) ──► Task 5 (subtitles)
                                      ├──► Task 7 (login UI)
                                      ├──► Task 8 (theme songs)
                                      ├──► Task 9 (search)
                                      └──► Task 10 (polish)
```
