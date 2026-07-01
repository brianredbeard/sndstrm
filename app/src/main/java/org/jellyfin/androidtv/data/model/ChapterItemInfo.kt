// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.data.model

import java.util.UUID

data class ChapterItemInfo(
	val itemId: UUID,
	val name: String?,
	val startPositionTicks: Long,
	val imagePath: String?,
)
