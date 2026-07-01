// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.preference.constant

import org.jellyfin.androidtv.R
import org.jellyfin.preference.PreferenceEnum

enum class AppTheme(
	override val nameRes: Int,
) : PreferenceEnum {
	PURPLE_HAZE(R.string.pref_theme_purple_haze),
	DARK(R.string.pref_theme_dark),
	EMERALD(R.string.pref_theme_emerald),
	MUTED_PURPLE(R.string.pref_theme_muted_purple),
	BASIC(R.string.pref_theme_basic),
	FLEXY(R.string.pref_theme_flexy),
	YELLOW_TOWN(R.string.pref_theme_yellow_town),
	DARK_PURPLE(R.string.pref_theme_dark_purple),
}
