// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.preference.migration

import android.content.SharedPreferences

fun <T : Enum<T>> SharedPreferences.Editor.putEnum(key: String, value: T) {
	putString(key, value.toString())
}
