// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.constant

import org.jellyfin.androidtv.R
import org.jellyfin.preference.PreferenceEnum

enum class ImageType(
	override val nameRes: Int,
) : PreferenceEnum {
	/**
	 * Poster.
	 */
	POSTER(R.string.image_type_poster),

	/**
	 * Thumbnail.
	 */
	THUMB(R.string.image_type_thumbnail),

	/**
	 * Banner.
	 */
	BANNER(R.string.image_type_banner),
}
