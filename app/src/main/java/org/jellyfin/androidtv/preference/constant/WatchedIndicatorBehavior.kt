// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.preference.constant

import org.jellyfin.androidtv.R
import org.jellyfin.preference.PreferenceEnum

enum class WatchedIndicatorBehavior(
	override val nameRes: Int,
) : PreferenceEnum {
	/**
	 * Always show watched indicators.
	 */
	ALWAYS(R.string.lbl_always),

	/**
	 * Hide unwatched count indicator, show watched check mark only.
	 */
	HIDE_UNWATCHED(R.string.lbl_hide_unwatched_count),

	/**
	 * Hide unwatched count indicator, show watched check mark on individual episodes only.
	 */
	EPISODES_ONLY(R.string.lbl_hide_watched_checkmark),

	/**
	 * Never show watched indicators.
	 */
	NEVER(R.string.lbl_never),
}
