// Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.ui.playback.model

/**
 * Data class to hold network statistics
 */
data class NetworkStats(
    val bytesRead: Long = 0,
    val bytesWritten: Long = 0,
    val timestamp: Long = System.currentTimeMillis()
)
