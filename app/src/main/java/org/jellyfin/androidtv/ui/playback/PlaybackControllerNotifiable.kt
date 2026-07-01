// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.ui.playback

interface PlaybackControllerNotifiable {
	fun onCompletion()
	fun onError()
	fun onPrepared()
	fun onProgress()
	fun onPlaybackSpeedChange(newSpeed: Float)
}
