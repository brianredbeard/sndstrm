// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.playback.core.plugin

import org.jellyfin.playback.core.backend.PlayerBackend
import org.jellyfin.playback.core.mediastream.MediaStreamResolver

fun interface PlaybackPlugin {
	fun install(context: InstallContext)

	interface InstallContext {
		fun provide(backend: PlayerBackend)
		fun provide(service: PlayerService)
		fun provide(mediaStreamResolver: MediaStreamResolver)
	}
}

fun playbackPlugin(init: PlaybackPlugin.InstallContext.() -> Unit) = PlaybackPlugin { context -> context.init() }
