// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.playback.core.backend

import org.jellyfin.playback.core.mediastream.PlayableMediaStream
import org.jellyfin.playback.core.model.PlayState

interface PlayerBackendEventListener {
	fun onPlayStateChange(state: PlayState)
	fun onVideoSizeChange(width: Int, height: Int)
	fun onMediaStreamEnd(mediaStream: PlayableMediaStream)
}
