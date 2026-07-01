// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.ui.player.base

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import org.jellyfin.playback.core.PlaybackManager
import org.jellyfin.playback.core.ui.PlayerSurfaceView
import org.koin.compose.koinInject

@Composable
fun PlayerSurface(
	modifier: Modifier = Modifier,
	playbackManager: PlaybackManager = koinInject(),
) {
	AndroidView(
		factory = { context -> PlayerSurfaceView(context) },
		modifier = modifier,
		update = { view ->
			view.playbackManager = playbackManager
		}
	)
}
