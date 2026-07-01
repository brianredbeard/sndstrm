// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.ui.navigation

import kotlinx.serialization.json.Json
import org.jellyfin.androidtv.constant.Extras
import org.jellyfin.androidtv.ui.browsing.BrowseGridFragment
import org.jellyfin.androidtv.ui.browsing.BrowseRecordingsFragment
import org.jellyfin.androidtv.ui.browsing.BrowseScheduleFragment
import org.jellyfin.androidtv.ui.browsing.BrowseViewFragment
import org.jellyfin.androidtv.ui.browsing.CollectionFragment
import org.jellyfin.androidtv.ui.browsing.GenericFolderFragment
import org.jellyfin.androidtv.ui.home.HomeFragment
import org.jellyfin.androidtv.ui.itemdetail.FullDetailsFragment
import org.jellyfin.androidtv.ui.itemdetail.ItemListFragment
import org.jellyfin.androidtv.ui.itemdetail.MusicFavoritesListFragment
import org.jellyfin.androidtv.ui.livetv.LiveTvGuideFragment
// PictureViewerFragment removed — upstream replaced with Compose
import org.jellyfin.androidtv.ui.playback.AudioNowPlayingFragment
import org.jellyfin.androidtv.ui.playback.CustomPlaybackOverlayFragment
import org.jellyfin.androidtv.ui.playback.nextup.NextUpFragment
import org.jellyfin.androidtv.ui.search.SearchFragment
import org.jellyfin.sdk.model.api.BaseItemDto
import org.jellyfin.sdk.model.api.ItemSortBy
import org.jellyfin.sdk.model.api.SeriesTimerInfoDto
import org.jellyfin.sdk.model.api.SortOrder
import java.util.UUID

@Suppress("TooManyFunctions")
object Destinations {
	// General
	val home = fragmentDestination<HomeFragment>()
	fun search(query: String? = null) = fragmentDestination<SearchFragment>(
		SearchFragment.EXTRA_QUERY to query,
	)

	// Browsing
	// TODO only pass item id instead of complete JSON to browsing destinations
	fun libraryBrowser(item: BaseItemDto) = fragmentDestination<BrowseGridFragment>(
		Extras.Folder to Json.Default.encodeToString(item),
	)

	// TODO only pass item id instead of complete JSON to browsing destinations
	fun libraryBrowser(item: BaseItemDto, includeType: String) =
		fragmentDestination<BrowseGridFragment>(
			Extras.Folder to Json.Default.encodeToString(item),
			Extras.IncludeType to includeType,
		)

	// TODO only pass item id instead of complete JSON to browsing destinations
	fun librarySmartScreen(item: BaseItemDto) = fragmentDestination<BrowseViewFragment>(
		Extras.Folder to Json.Default.encodeToString(item),
	)

	// TODO only pass item id instead of complete JSON to browsing destinations
	fun collectionBrowser(item: BaseItemDto) = fragmentDestination<CollectionFragment>(
		Extras.Folder to Json.Default.encodeToString(item),
	)

	// TODO only pass item id instead of complete JSON to browsing destinations
	fun folderBrowser(item: BaseItemDto) = fragmentDestination<GenericFolderFragment>(
		Extras.Folder to Json.Default.encodeToString(item),
	)
	// Item details
	fun itemDetails(item: UUID) = fragmentDestination<FullDetailsFragment>(
		"ItemId" to item.toString(),
	)

	// TODO only pass item id instead of complete JSON to browsing destinations
	fun channelDetails(item: UUID, channel: UUID, programInfo: BaseItemDto) =
		fragmentDestination<FullDetailsFragment>(
			"ItemId" to item.toString(),
			"ChannelId" to channel.toString(),
			"ProgramInfo" to Json.Default.encodeToString(programInfo),
		)

	// TODO only pass item id instead of complete JSON to browsing destinations
	fun seriesTimerDetails(item: UUID, seriesTimer: SeriesTimerInfoDto) =
		fragmentDestination<FullDetailsFragment>(
			"ItemId" to item.toString(),
			"SeriesTimer" to Json.Default.encodeToString(seriesTimer),
		)

	fun itemList(item: UUID) = fragmentDestination<ItemListFragment>(
		"ItemId" to item.toString(),
	)

	fun musicFavorites(parent: UUID) = fragmentDestination<MusicFavoritesListFragment>(
		"ParentId" to parent.toString(),
	)

	// Favorites
	val favorites = fragmentDestination<org.jellyfin.androidtv.ui.browsing.FavoritesFragment>()

	// Live TV
	val liveTvGuide = fragmentDestination<LiveTvGuideFragment>()
	val liveTvSchedule = fragmentDestination<BrowseScheduleFragment>()
	val liveTvRecordings = fragmentDestination<BrowseRecordingsFragment>()
	val liveTvSeriesRecordings = fragmentDestination<BrowseViewFragment>(Extras.IsLiveTvSeriesRecordings to true)

	// Playback
	val nowPlaying = fragmentDestination<AudioNowPlayingFragment>()

	// pictureViewer removed — PictureViewerFragment not available

	fun videoPlayer(position: Int?) = fragmentDestination<CustomPlaybackOverlayFragment>(
		"Position" to (position ?: 0)
	)

	fun nextUp(item: UUID) = fragmentDestination<NextUpFragment>(
		NextUpFragment.ARGUMENT_ITEM_ID to item.toString()
	)
}
