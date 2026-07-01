// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.playback.core.model

data class VideoSize(
	val width: Int,
	val height: Int,
) {
	val aspectRatio: Float get() = width.toFloat() / height.toFloat()

	companion object {
		val EMPTY = VideoSize(0, 0)
	}
}
