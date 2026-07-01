// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.playback.media3.exoplayer

import android.content.Context
import org.jellyfin.playback.core.plugin.playbackPlugin

fun exoPlayerPlugin(
	androidContext: Context,
	exoPlayerOptions: ExoPlayerOptions = ExoPlayerOptions(),
) = playbackPlugin {
	provide(ExoPlayerBackend(androidContext, exoPlayerOptions))
}
