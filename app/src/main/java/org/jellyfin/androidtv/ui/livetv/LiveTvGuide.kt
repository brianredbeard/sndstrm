// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.ui.livetv

import android.widget.RelativeLayout
import java.time.LocalDateTime
import java.util.UUID

interface LiveTvGuide {
	fun displayChannels(start: Int, max: Int)
	fun getCurrentLocalStartDate(): LocalDateTime
	fun showProgramOptions()
	fun setSelectedProgram(programView: RelativeLayout)
	fun refreshFavorite(channelId: UUID)
}
