# sndstrm

A maintained fork of the [Jellyfin](https://jellyfin.org/) Android TV client
with enhanced UI/UX and additional features.

> **This is an unofficial, community-maintained fork.** It is not affiliated
> with the Jellyfin project. The official client is at
> [jellyfin/jellyfin-androidtv](https://github.com/jellyfin/jellyfin-androidtv).

## Lineage

sndstrm continues the work of [DUNE](https://github.com/Sam42a/DUNE) (by
Sam42a), itself a fork of the official Jellyfin Android TV client. Full history
is documented in [FORK.md](FORK.md).

## Features

### Visual & Interface
- Redesigned home screen with improved content hierarchy
- OLED-optimized dark mode and multiple theme options
- Dynamic color extraction from backdrop images
- DreamView screensaver with smooth transitions
- Enhanced login screen with default avatars and visual feedback

### Home Screen
- Customizable home sections and genre rows
- "Because You Watched" suggested content row
- Combined continue watching / next up row
- Vertical alphabet sidebar for grid browsing
- Launcher channel integration

### Playback
- Episode selector in playback overlay
- Hardware acceleration toggle
- Audio downmix auto-fallback
- Chapter support in overlay
- Configurable skip duration

### Subtitles
- Subtitle download functionality
- TX3G/MOV_TEXT subtitle support
- Default subtitle language preference
- Subtitle preview

### Settings & Customization
- Configurable image disk cache size
- In-app language selector
- Genre manager with sorting preferences
- Preferences UI migrated to Jetpack Compose

## Building from Source

### Requirements
- Android Studio (latest stable)
- JDK 21
- Android SDK with API level matching `compileSdk` in `gradle/libs.versions.toml`

### Build Instructions

```bash
git clone https://github.com/brianredbeard/sndstrm.git
cd sndstrm
./gradlew assembleStandardDebug
```

For the enhanced variant (separate package ID, can be installed alongside
official Jellyfin):

```bash
./gradlew buildEnhanced
```

### Install on Device

```bash
# Debug build
adb install app/build/outputs/apk/standard/debug/sndstrm-androidtv-*.apk

# Enhanced build
adb install app/build/outputs/apk/enhanced/release/sndstrm-androidtv-0.1.1.apk
```

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines. By contributing, you
agree that your contributions will be licensed under GPL-2.0.

## Acknowledgments

- [Jellyfin](https://jellyfin.org/) — the upstream project and all its
  contributors (1,044 authors across 9,000+ commits)
- [Sam42a](https://github.com/Sam42a) — creator of DUNE, the intermediate fork
- [OLED theme](https://github.com/LitCastVlog/jellyfin-androidtv-OLED) — basis
  for the OLED-optimized dark mode

## License

Licensed under [GPL-2.0](LICENSE). See [NOTICE](NOTICE) for full copyright
attribution and [FORK.md](FORK.md) for fork history.
