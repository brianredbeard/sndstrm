// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.integration.dream.model

import android.graphics.Bitmap
import org.jellyfin.playback.core.queue.QueueEntry
import org.jellyfin.sdk.model.api.BaseItemDto

sealed interface DreamContent {
	data object Logo : DreamContent
	data class LibraryShowcase(val item: BaseItemDto, val backdrop: Bitmap, val logo: Bitmap?) : DreamContent
	data class NowPlaying(val entry: QueueEntry, val item: BaseItemDto) : DreamContent
}
