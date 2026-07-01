// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.playback.media3.session

import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

class AndroidMediaService : MediaSessionService() {
	override fun onGetSession(
		controllerInfo: MediaSession.ControllerInfo,
	): MediaSession? = sessions.firstOrNull()
}
