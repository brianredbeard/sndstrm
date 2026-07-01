// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.auth.model

import org.jellyfin.androidtv.R
import org.jellyfin.preference.PreferenceEnum

enum class AuthenticationSortBy(
	override val nameRes: Int
) : PreferenceEnum {
	LAST_USE(R.string.last_use),
	ALPHABETICAL(R.string.alphabetical);
}
