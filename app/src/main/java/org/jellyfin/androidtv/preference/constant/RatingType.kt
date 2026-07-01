// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.preference.constant

import org.jellyfin.androidtv.R
import org.jellyfin.preference.PreferenceEnum

enum class RatingType(
	override val nameRes: Int,
) : PreferenceEnum {
	/**
	 * Sets default rating type to tomatoes.
	 */
	RATING_TOMATOES(R.string.lbl_tomatoes),

	/**
	 * Sets the default rating type to stars.
	 */
	RATING_STARS(R.string.lbl_stars),

	/**
	 * Sets the default rating type to hidden.
	 */
	RATING_HIDDEN(R.string.lbl_hidden),
}
