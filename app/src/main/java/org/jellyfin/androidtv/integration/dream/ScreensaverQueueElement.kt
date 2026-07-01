// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.integration.dream

import org.jellyfin.playback.core.element.ElementKey
import org.jellyfin.playback.core.element.requiredElement
import org.jellyfin.playback.core.queue.QueueEntry

private val visibleInScreensaverKey = ElementKey<Boolean>("visibleInScreensaver")

/**
 * Get or set whether this [QueueEntry] is shown in the screensaver.
 */
var QueueEntry.visibleInScreensaver by requiredElement(visibleInScreensaverKey) { false }
