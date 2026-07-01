// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.playback.core.mediastream

sealed interface MediaConversionMethod {
	data object None : MediaConversionMethod
	data object Remux : MediaConversionMethod
	data object Transcode : MediaConversionMethod
}
