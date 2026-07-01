// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.data.model

import org.jellyfin.sdk.model.api.ItemFilter


class FilterOptions {
	var isFavoriteOnly = false
	var isUnwatchedOnly = false

	val filters: Set<ItemFilter>
		get() = buildSet {
			if (isFavoriteOnly) {
				add(ItemFilter.IS_FAVORITE)
			} else if (isUnwatchedOnly) {
				add(ItemFilter.IS_UNPLAYED)
			}
		}
}
