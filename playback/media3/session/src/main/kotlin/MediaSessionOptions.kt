// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.playback.media3.session

import android.app.PendingIntent
import androidx.annotation.DrawableRes

data class MediaSessionOptions(
	val channelId: String,
	val notificationId: Int,
	@DrawableRes val iconSmall: Int,
	val openIntent: PendingIntent,
)
