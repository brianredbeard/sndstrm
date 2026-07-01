// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.ui.composable

import androidx.compose.ui.MotionDurationScale

/**
 * A [MotionDurationScale] implementation that always returns a fixed scale factor of 1f. To be used for animations that should ignore the
 * system animator duration scale.
 */
object FixedMotionDurationScale : MotionDurationScale {
	override val scaleFactor: Float = 1f
}
