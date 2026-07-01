// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.ui.playback

import org.jellyfin.sdk.model.api.BaseItemDto

interface AudioEventListener {
	fun onPlaybackStateChange(newState: PlaybackController.PlaybackState, currentItem: BaseItemDto?) = Unit
	fun onProgress(pos: Long, duration: Long) = Unit
	fun onQueueStatusChanged(hasQueue: Boolean) = Unit
	fun onQueueReplaced() = Unit
}
