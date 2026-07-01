// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.ui.playback.overlay.action

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import org.jellyfin.androidtv.R
import org.jellyfin.androidtv.ui.playback.PlaybackController
import org.jellyfin.androidtv.ui.playback.overlay.CustomPlaybackTransportControlGlue
import org.jellyfin.androidtv.ui.playback.overlay.LeanbackOverlayFragment
import org.jellyfin.androidtv.ui.playback.overlay.VideoPlayerAdapter

class RecordAction(
	context: Context,
	customPlaybackTransportControlGlue: CustomPlaybackTransportControlGlue,
) : CustomAction(context, customPlaybackTransportControlGlue) {
	companion object {
		const val INDEX_INACTIVE = 0
		const val INDEX_RECORDING = 1
	}

	init {
		val recordInactive = ContextCompat.getDrawable(context, R.drawable.ic_record)
		val recordActive = ContextCompat.getDrawable(context, R.drawable.ic_record_red)

		setDrawables(arrayOf(recordInactive, recordActive))
	}

	@Override
	override fun handleClickAction(
		playbackController: PlaybackController,
		videoPlayerAdapter: VideoPlayerAdapter,
		context: Context,
		view: View,
	) {
		videoPlayerAdapter.toggleRecording()
	}
}
