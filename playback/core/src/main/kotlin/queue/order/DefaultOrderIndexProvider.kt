// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.playback.core.queue.order

import kotlin.math.min

internal class DefaultOrderIndexProvider : OrderIndexProvider {
	override fun provideIndices(
		amount: Int,
		size: Int,
		playedIndices: Collection<Int>,
		currentIndex: Int,
	): Collection<Int> {
		// No need to use currentQueueNextIndices because we can efficiently calculate the next items
		val remainingItemsSize = size - currentIndex - 1

		return if (remainingItemsSize <= 0) emptyList()
		else Array(min(amount, remainingItemsSize)) { i -> currentIndex + i + 1 }.toList()
	}
}
