# Fork History

**sndstrm** is a maintained fork of the Jellyfin Android TV client with enhanced
UI/UX features. This document records the project's lineage and satisfies
GPL-2.0 §2(a) change-disclosure requirements.

## Lineage

```
jellyfin/jellyfin-androidtv (GPL-2.0)
  ↓  forked June 2025 at commit 5391bcf1b
Sam42a/DUNE
  ↓  forked June 2026 for maintenance + GPL-2.0 compliance
brianredbeard/sndstrm (this repo)
```

## Upstream

- **Project:** Jellyfin Android TV
- **Repository:** https://github.com/jellyfin/jellyfin-androidtv
- **License:** GPL-2.0
- **Fork point commit:** `5391bcf1bde95bc70a3b7f1244117b142d1dd114`
  - Author: Niels van Velzen
  - Date: 2025-08-05
  - Message: "Add compose based video player"
  - Tagged in this repo as: `upstream-fork-point`

## Intermediate Fork (DUNE)

- **Repository:** https://github.com/Sam42a/DUNE
- **Author:** Sam42a
- **Active period:** June 2025 – January 2026
- **Total contributions:** ~394 commits across UI theming, home screen
  customization, playback enhancements, subtitle support, and Compose migrations

## Original Contributors

The full upstream commit history (9,231 commits, 1,044 authors) is preserved in
this repository. All original Jellyfin contributors retain their commit
attribution. The complete list of upstream contributors is available at:
https://github.com/jellyfin/jellyfin-androidtv/graphs/contributors

A partial list is maintained in [CONTRIBUTORS.md](CONTRIBUTORS.md).

## Summary of Modifications

Changes from upstream jellyfin-androidtv, introduced by DUNE and continued by
sndstrm:

- **UI/Theme system** — OLED-optimized themes, dynamic color extraction,
  DreamView screensaver, redesigned carousel and card components
- **Home screen** — Custom sections, "Because You Watched" row, combined
  continue watching / next up, alphabet sidebar for grid browsing
- **Playback** — Episode selector overlay, hardware acceleration toggle, audio
  downmix fallback, chapter support, configurable skip duration
- **Subtitles** — Download functionality, TX3G/MOV_TEXT support, language
  defaults, subtitle preview
- **Settings** — Configurable disk cache, genre manager, in-app language
  selector, preference UI migrated to Jetpack Compose
- **Login** — Redesigned server/user selection, default avatars, focus management
- **Branding** — Renamed from Jellyfin ATV → DUNE → sndstrm, decoupled
  telemetry, independent update channel

## How to View the Full Diff

```bash
# Diff between upstream fork point and current HEAD
git diff upstream-fork-point..HEAD

# Diff stats only
git diff --stat upstream-fork-point..HEAD
```
