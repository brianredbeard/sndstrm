// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.util

import android.os.Build
import android.os.Bundle

@Suppress("DEPRECATION")
inline fun <reified T> Bundle.getValue(key: String): T? = when {
	Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelable(key, T::class.java)
	else -> get(key) as T?
}
