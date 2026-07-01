// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.playback.core.mediastream

import org.jellyfin.playback.core.queue.QueueEntry
import org.jellyfin.playback.core.support.PlaySupportReport

/**
 * Determine the media stream for a given queue item.
 */
interface MediaStreamResolver {
	/**
	 * @return [PlayableMediaStream] or null if no stream can be determined by this resolver
	 */
	suspend fun getStream(
		queueEntry: QueueEntry,
		testStream: (stream: MediaStream) -> PlaySupportReport,
	): PlayableMediaStream?
}
