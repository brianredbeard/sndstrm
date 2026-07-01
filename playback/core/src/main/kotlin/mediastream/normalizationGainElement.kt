// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.playback.core.mediastream

import org.jellyfin.playback.core.element.ElementKey
import org.jellyfin.playback.core.element.element
import org.jellyfin.playback.core.element.elementFlow
import org.jellyfin.playback.core.queue.QueueEntry

private val normalizationGainKey = ElementKey<Float>("NormalizationGain")

/**
 * Get or set the normalization gain for this [QueueEntry]. A supported backend will use this to
 * apply a gain to the audio output. The normalization gain must target a loudness of -23LUFS.
 */
var QueueEntry.normalizationGain by element(normalizationGainKey)

/**
 * Get the flow of [normalizationGain].
 * @see normalizationGain
 */
val QueueEntry.normalizationGainFlow by elementFlow(normalizationGainKey)
