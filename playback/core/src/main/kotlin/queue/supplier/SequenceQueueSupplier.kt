// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.playback.core.queue.supplier

import org.jellyfin.playback.core.queue.QueueEntry

abstract class SequenceQueueSupplier : QueueSupplier {
	companion object {
		const val MAX_SIZE = 100
	}

	protected abstract val items: Sequence<QueueEntry>
	private val itemIterator by lazy { items.iterator() }
	private val buffer = mutableListOf<QueueEntry>()

	override suspend fun getItem(index: Int): QueueEntry? {
		require(index in 0 until MAX_SIZE)

		do {
			// Buffer contains the requested item
			if (buffer.size > index) return buffer[index]
			// Requested item is too big
			if (!itemIterator.hasNext()) return null
			// Add next item to buffer
			buffer.add(itemIterator.next())
		} while (true)
	}
}
