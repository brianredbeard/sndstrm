// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.ui.preference.screen

import android.widget.Toast
import org.jellyfin.androidtv.R
import org.jellyfin.androidtv.preference.UserPreferences
import org.jellyfin.androidtv.preference.UserSettingPreferences
import org.jellyfin.androidtv.ui.itemdetail.ArchiveHelper
import org.jellyfin.androidtv.ui.preference.dsl.OptionsFragment
import org.jellyfin.androidtv.ui.preference.dsl.action
import org.jellyfin.androidtv.ui.preference.dsl.checkbox
import org.jellyfin.androidtv.ui.preference.dsl.optionsScreen
import org.koin.android.ext.android.inject

class ThemeSongPreferencesScreen : OptionsFragment() {
	private val userPreferences: UserPreferences by inject()
	private val userSettingPreferences: UserSettingPreferences by inject()

	override val screen by optionsScreen {
		setTitle(R.string.pref_theme_songs)

		category {
			setTitle(R.string.pref_theme_songs_enable)

			checkbox {
				setTitle(R.string.pref_theme_songs_enable)
				bind(userSettingPreferences, userSettingPreferences.themeSongsEnabled)
			}
		}

		category {
			setTitle(R.string.pref_theme_song_media_types)

			checkbox {
				setTitle(R.string.pref_theme_song_movies)
				setContent(R.string.pref_theme_song_movies_summary)
				bind(userSettingPreferences, userSettingPreferences.themeSongsMovies)
				depends { userSettingPreferences[userSettingPreferences.themeSongsEnabled] }
			}

			checkbox {
				setTitle(R.string.pref_theme_song_series)
				setContent(R.string.pref_theme_song_series_summary)
				bind(userSettingPreferences, userSettingPreferences.themeSongsSeries)
				depends { userSettingPreferences[userSettingPreferences.themeSongsEnabled] }
			}

			checkbox {
				setTitle(R.string.pref_theme_song_episodes)
				setContent(R.string.pref_theme_song_episodes_summary)
				bind(userSettingPreferences, userSettingPreferences.themeSongsEpisodes)
				depends { userSettingPreferences[userSettingPreferences.themeSongsEnabled] }
			}
		}

		category {
			setTitle(R.string.advanced_settings)

			checkbox {
				setTitle(R.string.pref_theme_song_archive_fallback)
				setContent(R.string.pref_theme_song_archive_fallback_summary)
				bind(userSettingPreferences, userSettingPreferences.themeSongsArchiveFallback)
				depends { userSettingPreferences[userSettingPreferences.themeSongsEnabled] }
			}

			checkbox {
				setTitle(R.string.pref_theme_song_cache)
				setContent(R.string.pref_theme_song_cache_summary)
				bind(userPreferences, UserPreferences.themeSongsCacheEnabled)
				depends {
					userSettingPreferences[userSettingPreferences.themeSongsEnabled] &&
						userSettingPreferences[userSettingPreferences.themeSongsArchiveFallback]
				}
			}

			checkbox {
				setTitle(R.string.pref_theme_song_cache_permanent)
				setContent(R.string.pref_theme_song_cache_permanent_summary)
				bind(userPreferences, UserPreferences.themeSongsCachePermanent)
				depends {
					userSettingPreferences[userSettingPreferences.themeSongsEnabled] &&
						userSettingPreferences[userSettingPreferences.themeSongsArchiveFallback] &&
						userPreferences[UserPreferences.themeSongsCacheEnabled]
				}
			}

			action {
				setTitle(R.string.pref_theme_song_clear_cache)
				setContent(R.string.pref_theme_song_clear_cache_summary)
				depends {
					userSettingPreferences[userSettingPreferences.themeSongsEnabled] &&
						userSettingPreferences[userSettingPreferences.themeSongsArchiveFallback] &&
						userPreferences[UserPreferences.themeSongsCacheEnabled]
				}
				onActivate = {
					val helper = ArchiveHelper(requireContext())
					val count = helper.getCacheSize()
					helper.clearCache()
					Toast.makeText(
						requireContext(),
						getString(R.string.pref_theme_song_cache_cleared, count),
						Toast.LENGTH_SHORT
					).show()
				}
			}
		}
	}
}
