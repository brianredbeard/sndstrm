// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.playback.media3.exoplayer.support

import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.RendererCapabilities

enum class AdaptiveSupport {
	SEAMLESS,
	NOT_SEAMLESS,
	NOT_SUPPORTED;

	companion object {
		@OptIn(UnstableApi::class)
		fun fromFlags(flags: Int) = when (RendererCapabilities.getAdaptiveSupport(flags)) {
			RendererCapabilities.ADAPTIVE_SEAMLESS -> SEAMLESS
			RendererCapabilities.ADAPTIVE_NOT_SEAMLESS -> NOT_SEAMLESS
			RendererCapabilities.ADAPTIVE_NOT_SUPPORTED -> NOT_SUPPORTED
			else -> null
		}
	}
}
