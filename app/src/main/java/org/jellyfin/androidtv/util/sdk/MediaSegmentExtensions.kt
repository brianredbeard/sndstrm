// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.util.sdk

import org.jellyfin.sdk.model.api.MediaSegmentDto
import org.jellyfin.sdk.model.extensions.ticks
import kotlin.time.Duration

val MediaSegmentDto.start get() = startTicks.ticks
val MediaSegmentDto.end get() = endTicks.ticks

val MediaSegmentDto.duration get() = (endTicks - startTicks).ticks.coerceAtLeast(Duration.ZERO)
