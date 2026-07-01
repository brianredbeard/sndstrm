// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.playback.jellyfin.queue

import org.jellyfin.playback.core.element.ElementKey
import org.jellyfin.playback.core.element.element
import org.jellyfin.playback.core.element.elementFlow
import org.jellyfin.playback.core.queue.QueueEntry
import org.jellyfin.sdk.model.api.BaseItemDto

private val baseItemKey = ElementKey<BaseItemDto>("BaseItemDto")

/**
 * Get or set the [BaseItemDto] for this [QueueEntry].
 */
var QueueEntry.baseItem by element(baseItemKey)

/**
 * Get the [BaseItemDto] flow for this [QueueEntry].
 */
val QueueEntry.baseItemFlow by elementFlow(baseItemKey)
