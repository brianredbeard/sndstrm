// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.playback.core.queue

import org.jellyfin.playback.core.element.ElementsContainer

/**
 * The QueueEntry is a single item in a queue and can represent any supported media type.
 * All related data is stored in elements via the [ElementsContainer].
 */
class QueueEntry : ElementsContainer()
