// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.util

import android.content.Context

/**
 * Current (pixel) value as display pixels
 */
fun Int.dp(context: Context): Int = Utils.convertDpToPixel(context, this)
