// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.playback.core.queue.supplier

import org.jellyfin.playback.core.queue.QueueEntry

/**
 * A queue contains all items in the current playback session. This includes already played items,
 * the currently playing item and future items.
 */
interface QueueSupplier {
	/**
	 * The total size of the queue.
	 */
	val size: Int

	suspend fun getItem(index: Int): QueueEntry?
}
