// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.ui.itemhandling

import org.jellyfin.sdk.model.api.BaseItemDto

class AudioQueueBaseRowItem(
	item: BaseItemDto,
) : BaseItemDtoBaseRowItem(
	item = item,
	staticHeight = true,
) {
	var playing: Boolean = false
}
