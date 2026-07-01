// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.ui.preference.screen

import org.jellyfin.androidtv.R
import org.jellyfin.androidtv.constant.HomeSectionType
import org.jellyfin.androidtv.preference.UserSettingPreferences
import org.jellyfin.androidtv.ui.preference.dsl.OptionsFragment
import org.jellyfin.androidtv.ui.preference.dsl.enum
import org.jellyfin.androidtv.ui.preference.dsl.optionsScreen
import org.jellyfin.preference.store.PreferenceStore
import org.koin.android.ext.android.inject

class HomePreferencesScreen : OptionsFragment() {
	private val userSettingPreferences: UserSettingPreferences by inject()

	override val stores: Array<PreferenceStore<*, *>>
		get() = arrayOf(userSettingPreferences)

	override val screen by optionsScreen {
		setTitle(R.string.home_prefs)

		category {
			setTitle(R.string.home_sections)

			userSettingPreferences.homesections.forEachIndexed { index, section ->
				enum<HomeSectionType> {
					title = getString(R.string.home_section_i, index + 1)
					bind(userSettingPreferences, section)
				}
			}
		}
	}
}
