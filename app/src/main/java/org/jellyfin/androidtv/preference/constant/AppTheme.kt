// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.preference.constant

import org.jellyfin.androidtv.R
import org.jellyfin.preference.PreferenceEnum

enum class AppTheme(
	override val nameRes: Int,
) : PreferenceEnum {
	/**
	 * The default dark theme
	 */
	DARK(R.string.pref_theme_dark),

	/**
	 * The "classic" emerald theme
	 */
	EMERALD(R.string.pref_theme_emerald),

	/**
	 * A theme with a more muted accent color, inspired by CTalvio's Monochromic CSS theme for Jellyfin Web
	 */
	MUTED_PURPLE(R.string.pref_theme_muted_purple),
}
