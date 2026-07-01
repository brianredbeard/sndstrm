// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.preference

import androidx.annotation.StringRes

interface PreferenceEnum {
	/**
	 * True to hide this option or false (default) to display.
	 */
	val hidden: Boolean get() = false

	/**
	 * The id of the name resource or -1 for "none".
	 */
	@get:StringRes
	val nameRes: Int

	/**
	 * The name used to store the preference or null to use the name property.
	 */
	val serializedName: String? get() = null
}
