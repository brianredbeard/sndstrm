// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.constant

import org.jellyfin.androidtv.R
import org.jellyfin.preference.PreferenceEnum

enum class PosterSize(
	override val nameRes: Int,
) : PreferenceEnum {
	/**
	 * Smallest.
	 */
	SMALLEST(R.string.image_size_smallest),
	/**
	 * Small.
	 */
	SMALL(R.string.image_size_small),

	/**
	 * Medium.
	 */
	MED(R.string.image_size_medium),

	/**
	 * Large.
	 */
	LARGE(R.string.image_size_large),

	/**
	 * Extra Large.
	 */
	X_LARGE(R.string.image_size_xlarge),
}
