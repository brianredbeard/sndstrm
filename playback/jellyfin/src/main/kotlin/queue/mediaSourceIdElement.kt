// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.playback.jellyfin.queue

import org.jellyfin.playback.core.element.ElementKey
import org.jellyfin.playback.core.element.element
import org.jellyfin.playback.core.element.elementFlow
import org.jellyfin.playback.core.queue.QueueEntry

private val mediaSourceIdKey = ElementKey<String>("MediaSource")

/**
 * Get or set the id of the MediaSource to use during playback. Or null for the default selection
 * behavior.
 */
var QueueEntry.mediaSourceId by element(mediaSourceIdKey)

/**
 * Get the flow of [mediaSourceId].
 * @see mediaSourceId
 */
val QueueEntry.mediaSourceIdFlow by elementFlow(mediaSourceIdKey)
