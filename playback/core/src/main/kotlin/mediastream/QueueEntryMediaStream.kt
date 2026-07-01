// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.playback.core.mediastream

import org.jellyfin.playback.core.element.ElementKey
import org.jellyfin.playback.core.element.element
import org.jellyfin.playback.core.element.elementFlow
import org.jellyfin.playback.core.queue.QueueEntry

private val mediaStreamKey = ElementKey<PlayableMediaStream>("MediaStream")

/**
 * Get or set the [MediaStream] for this [QueueEntry].
 */
var QueueEntry.mediaStream by element(mediaStreamKey)

/**
 * Get the [MediaStream] flow for this [QueueEntry].
 */
val QueueEntry.mediaStreamFlow by elementFlow(mediaStreamKey)
