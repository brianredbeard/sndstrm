// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
@file:JvmName("JavaCompat")

package org.jellyfin.androidtv.util.sdk.compat

import org.jellyfin.sdk.model.api.BaseItemDto
import org.jellyfin.sdk.model.api.MediaSourceInfo
import org.jellyfin.sdk.model.api.UserItemDataDto
import java.time.LocalDateTime
import java.util.UUID

fun BaseItemDto.copyWithDisplayPreferencesId(
	displayPreferencesId: String?
) = copy(
	displayPreferencesId = displayPreferencesId,
)

fun BaseItemDto.copyWithDates(
	premiereDate: LocalDateTime?,
	endDate: LocalDateTime?,
	officialRating: String?,
	runTimeTicks: Long?,
) = copy(
	premiereDate = premiereDate,
	endDate = endDate,
	officialRating = officialRating,
	runTimeTicks = runTimeTicks,
)

fun BaseItemDto.copyWithTimerId(
	seriesTimerId: String?,
) = copy(
	seriesTimerId = seriesTimerId,
)

fun BaseItemDto.copyWithOverview(
	overview: String?,
) = copy(
	overview = overview,
)

fun BaseItemDto.copyWithParentId(
	parentId: UUID?,
) = copy(
	parentId = parentId,
)

fun BaseItemDto.copyWithUserData(
	userData: UserItemDataDto?,
) = copy(
	userData = userData,
)

fun MediaSourceInfo.getVideoStream() = mediaStreams?.firstOrNull {
	it.type == org.jellyfin.sdk.model.api.MediaStreamType.VIDEO
}

val BaseItemDto.canResume get() = (userData?.playbackPositionTicks ?: 0) > 0
