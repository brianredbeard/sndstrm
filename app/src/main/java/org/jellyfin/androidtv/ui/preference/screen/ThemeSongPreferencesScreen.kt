// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.ui.preference.screen

import org.jellyfin.androidtv.R
import org.jellyfin.androidtv.preference.UserPreferences
import org.jellyfin.androidtv.ui.preference.dsl.OptionsFragment
import org.jellyfin.androidtv.ui.preference.dsl.checkbox
import org.jellyfin.androidtv.ui.preference.dsl.optionsScreen
import org.koin.android.ext.android.inject

class ThemeSongPreferencesScreen : OptionsFragment() {
	private val userPreferences: UserPreferences by inject()

	override val screen by optionsScreen {
		setTitle(R.string.pref_theme_songs)

		category {
			setTitle(R.string.pref_theme_songs_enable)

			checkbox {
				setTitle(R.string.pref_theme_songs_enable)
				bind(userPreferences, UserPreferences.themeSongsEnabled)
			}
		}

		category {
			setTitle(R.string.pref_theme_song_media_types)

			checkbox {
				setTitle(R.string.pref_theme_song_movies)
				setContent(R.string.pref_theme_song_movies_summary)
				bind(userPreferences, UserPreferences.themeSongsMovies)
				depends { userPreferences[UserPreferences.themeSongsEnabled] }
			}

			checkbox {
				setTitle(R.string.pref_theme_song_series)
				setContent(R.string.pref_theme_song_series_summary)
				bind(userPreferences, UserPreferences.themeSongsSeries)
				depends { userPreferences[UserPreferences.themeSongsEnabled] }
			}

			checkbox {
				setTitle(R.string.pref_theme_song_episodes)
				setContent(R.string.pref_theme_song_episodes_summary)
				bind(userPreferences, UserPreferences.themeSongsEpisodes)
				depends { userPreferences[UserPreferences.themeSongsEnabled] }
			}
		}

		category {
			setTitle(R.string.advanced_settings)

			checkbox {
				setTitle(R.string.pref_theme_song_archive_fallback)
				setContent(R.string.pref_theme_song_archive_fallback_summary)
				bind(userPreferences, UserPreferences.themeSongsArchiveFallback)
				depends { userPreferences[UserPreferences.themeSongsEnabled] }
			}
		}
	}
}
