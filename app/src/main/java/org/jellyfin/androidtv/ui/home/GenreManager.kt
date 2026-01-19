package org.jellyfin.androidtv.ui.home

import android.content.Context
import androidx.leanback.widget.BaseCardView
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.Row
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.jellyfin.androidtv.R
import org.jellyfin.androidtv.auth.repository.UserRepository
import org.jellyfin.androidtv.constant.GenreRowType
import org.jellyfin.androidtv.data.repository.ItemRepository
import org.jellyfin.androidtv.preference.UserPreferences
import org.jellyfin.androidtv.preference.UserSettingPreferences
import org.jellyfin.androidtv.ui.browsing.BrowseRowDef
import org.jellyfin.androidtv.ui.card.LegacyImageCardView
import org.jellyfin.androidtv.ui.presentation.CardPresenter
import org.jellyfin.androidtv.ui.presentation.MutableObjectAdapter
import org.jellyfin.sdk.api.client.ApiClient
import org.jellyfin.sdk.model.api.BaseItemKind
import org.jellyfin.sdk.model.api.ItemFilter
import org.jellyfin.sdk.model.api.ItemSortBy
import org.jellyfin.sdk.model.api.SortOrder
import org.jellyfin.sdk.model.api.request.GetItemsRequest
import timber.log.Timber

class GenreManager(
	private val context: Context,
	private val userRepository: UserRepository,
	private val userPreferences: UserPreferences,
	private val userSettingPreferences: UserSettingPreferences,
	private val api: ApiClient
) {
	companion object {
		private const val GENRE_CARD_HEIGHT = 150
		private const val GENRE_CARD_THUMB = 200
	}
	data class GenreConfig(
		val name: String,
		val displayName: String,
		val preference: org.jellyfin.preference.Preference<Boolean>?,
		val loader: () -> HomeFragmentRow,
		val isNullable: Boolean = false
	)

	private val genreConfigs by lazy {
		listOf(
			GenreConfig(
				name = context.getString(R.string.show_collections_row),
				displayName = context.getString(R.string.show_collections_row),
				preference = null,
				loader = ::createCollectionsRow
			),
			GenreConfig(
				name = context.getString(R.string.show_discover_movies_row),
				displayName = context.getString(R.string.show_discover_movies_row),
				preference = null,
				loader = ::createDiscoverMoviesRow
			),
			GenreConfig(
				name = context.getString(R.string.show_discover_series_row),
				displayName = context.getString(R.string.show_discover_series_row),
				preference = null,
				loader = ::createDiscoverSeriesRow
			),
			GenreConfig(
				name = context.getString(R.string.show_recently_released_row),
				displayName = context.getString(R.string.show_recently_released_row),
				preference = null,
				loader = ::createRecentlyReleasedRow
			),
			GenreConfig(
				name = context.getString(R.string.show_watch_it_again_row),
				displayName =context.getString(R.string.show_watch_it_again_row),
				preference = null,
				loader = ::createWatchItAgainRow
			),
			GenreConfig(
				name =  context.getString(R.string.show_music_videos_row),
				displayName =  context.getString(R.string.show_music_videos_row),
				preference = null,
				loader = ::createMusicVideosRow
			),
			GenreConfig(
				name = context.getString(R.string.because_you_watched),
				displayName = context.getString(R.string.because_you_watched),
				preference = null,
				loader = ::createSuggestedMoviesRow
			),
			GenreConfig(
				name = context.getString(R.string.show_recently_episodes_row),
				displayName = context.getString(R.string.show_recently_episodes_row),
				preference = null,
				loader = ::createEpisodeRow

			)
		)
	}

	fun getEnabledGenres(): List<GenreConfig> {
		return computeEnabledGenresFromSlots()
	}

	private fun computeEnabledGenresFromSlots(): List<GenreConfig> {

		val genreSlots = listOf(
			userSettingPreferences.genrerow0,
			userSettingPreferences.genrerow1,
			userSettingPreferences.genrerow2,
			userSettingPreferences.genrerow3,
			userSettingPreferences.genrerow4,
			userSettingPreferences.genrerow5,
			userSettingPreferences.genrerow6,
			userSettingPreferences.genrerow7,
			userSettingPreferences.genrerow8,
			userSettingPreferences.genrerow9
		)

		val addedGenres = mutableSetOf<String>()
		val enabledGenres = mutableListOf<GenreConfig>()

		for ((index, slotPref) in genreSlots.withIndex()) {
			val slotValue = userSettingPreferences[slotPref]

			if (slotValue == GenreRowType.NONE) {
				continue
			}
			val config = when (slotValue) {
				GenreRowType.SUGGESTED_MOVIES -> genreConfigs.find { it.name == context.getString(R.string.because_you_watched) }
				GenreRowType.COLLECTIONS -> genreConfigs.find { it.name == context.getString(R.string.show_collections_row) }
				GenreRowType.DISCOVER_MOVIES -> genreConfigs.find { it.name == context.getString(R.string.show_discover_movies_row) }
				GenreRowType.DISCOVER_SERIES -> genreConfigs.find { it.name == context.getString(R.string.show_discover_series_row) }
				GenreRowType.RECENTLY_RELEASED -> genreConfigs.find { it.name == context.getString(R.string.show_recently_released_row) }
				GenreRowType.WATCH_IT_AGAIN -> genreConfigs.find { it.name == context.getString(R.string.show_watch_it_again_row) }
				GenreRowType.MUSIC -> genreConfigs.find { it.name == context.getString(R.string.show_music_videos_row) }
				GenreRowType.Episode -> genreConfigs.find { it.name == context.getString(R.string.show_recently_episodes_row) }
				else -> null
			}

			if (config != null && !addedGenres.contains(config.name)) {
				enabledGenres.add(config)
				addedGenres.add(config.name)
			}
		}

		return enabledGenres
	}


	suspend fun loadGenreRows(
		cardPresenter: CardPresenter,
		rowsAdapter: MutableObjectAdapter<Row>
	) = withContext(Dispatchers.IO) {
		val startTime = System.currentTimeMillis()

		try {
			val enabledGenres = getEnabledGenres()

			if (enabledGenres.isEmpty()) {
				return@withContext
			}

			val genreRowResults = enabledGenres.map { config ->
				async(Dispatchers.IO) {
					try {
						val row = config.loader()
						Pair(config, row)
					} catch (e: Exception) {
						Timber.e(e, "Error loading genre row: ${config.displayName}")
						Pair(config, null)
					}
				}
			}.awaitAll()

			withContext(Dispatchers.Main) {
				genreRowResults.forEach { (config, row) ->
					try {
						if (config.isNullable && row == null) {
						} else {
							row?.addToRowsAdapter(context, cardPresenter, rowsAdapter)
						}
					} catch (e: Exception) {
						Timber.e(e, "Error adding genre row to adapter: ${config.displayName}")
					}
				}
			}

			val loadTime = System.currentTimeMillis() - startTime
		} catch (e: Exception) {
			Timber.e(e, "Error loading genre rows")
		}
	}

	private fun createSuggestedMoviesRow(): HomeFragmentRow {
		return HomeFragmentSuggestedMoviesFragmentRow(userRepository, api)
	}

	private fun createMusicVideosRow(): HomeFragmentRow {
		return HomeFragmentMusicVideosRow(userRepository, api)
	}

	private fun createCollectionsRow(): HomeFragmentRow {
		val currentUserId = requireNotNull(userRepository.currentUser.value?.id) {
			"User not available"
		}

		val query = GetItemsRequest(
			userId = currentUserId,
			includeItemTypes = listOf(BaseItemKind.BOX_SET),
			sortBy = setOf(ItemSortBy.DATE_CREATED),
			sortOrder = listOf(SortOrder.DESCENDING),
			limit = getGenreItemLimit(),
			recursive = true,
			imageTypeLimit = 1,
			enableTotalRecordCount = false,
			fields = ItemRepository.itemFields,
			enableImages = true
		)

		return object : HomeFragmentRow {
			override fun addToRowsAdapter(
				context: Context,
				cardPresenter: CardPresenter,
				rowsAdapter: MutableObjectAdapter<Row>
			) {
				val collectionsCardPresenter = createUniformCardPresenter()
				val rowDef = BrowseRowDef(
					context.getString(R.string.show_collections_row),
					query,
					getGenreItemLimit()
				)

				HomeFragmentBrowseRowDefRow(rowDef).addToRowsAdapter(
					context,
					collectionsCardPresenter,
					rowsAdapter
				)
			}
		}
	}

	private fun createDiscoverMoviesRow(): HomeFragmentRow {
		val currentUserId = requireNotNull(userRepository.currentUser.value?.id) {
			"User not available"
		}

		val discoverMoviesQuery = GetItemsRequest(
			userId = currentUserId,
			includeItemTypes = listOf(BaseItemKind.MOVIE),
			sortBy = setOf(ItemSortBy.RANDOM),
			filters = setOf(ItemFilter.IS_UNPLAYED),
			sortOrder = listOf(SortOrder.DESCENDING),
			limit = getGenreItemLimit(),
			recursive = true,
			imageTypeLimit = 1,
			enableTotalRecordCount = false,
			fields = ItemRepository.itemFields,
			enableImages = true
		)

		return object : HomeFragmentRow {
			override fun addToRowsAdapter(
				context: Context,
				cardPresenter: CardPresenter,
				rowsAdapter: MutableObjectAdapter<Row>
			) {
				val discoverMoviesCardPresenter = createUniformCardPresenter()
				val rowDef = BrowseRowDef(
					context.getString(R.string.show_discover_movies_row),
					discoverMoviesQuery,
					getGenreItemLimit()
				)

				HomeFragmentBrowseRowDefRow(rowDef).addToRowsAdapter(
					context,
					discoverMoviesCardPresenter,
					rowsAdapter
				)
			}
		}
	}

	private fun createDiscoverSeriesRow(): HomeFragmentRow {
		val currentUserId = requireNotNull(userRepository.currentUser.value?.id) {
			"User not available"
		}

		val discoverSeriesQuery = GetItemsRequest(
			userId = currentUserId,
			includeItemTypes = listOf(BaseItemKind.SERIES),
			sortBy = setOf(ItemSortBy.RANDOM),
			sortOrder = listOf(SortOrder.DESCENDING),
			limit = getGenreItemLimit(),
			recursive = true,
			imageTypeLimit = 1,
			enableTotalRecordCount = false,
			fields = ItemRepository.itemFields,
			enableImages = true
		)

		return object : HomeFragmentRow {
			override fun addToRowsAdapter(
				context: Context,
				cardPresenter: CardPresenter,
				rowsAdapter: MutableObjectAdapter<Row>
			) {
				val discoverSeriesCardPresenter = createUniformCardPresenter()
				val rowDef = BrowseRowDef(
					context.getString(R.string.show_discover_series_row),
					discoverSeriesQuery,
					getGenreItemLimit()
				)

				HomeFragmentBrowseRowDefRow(rowDef).addToRowsAdapter(
					context,
					discoverSeriesCardPresenter,
					rowsAdapter
				)
			}
		}
	}

	private fun createRecentlyReleasedRow(): HomeFragmentRow {
		val currentUserId = requireNotNull(userRepository.currentUser.value?.id) {
			"User not available"
		}

		val recentlyReleasedQuery = GetItemsRequest(
			userId = currentUserId,
			includeItemTypes = listOf(BaseItemKind.MOVIE),
			sortBy = listOf(ItemSortBy.PREMIERE_DATE),
			sortOrder = listOf(SortOrder.DESCENDING),
			filters = setOf(ItemFilter.IS_UNPLAYED),
			limit = getGenreItemLimit(),
			recursive = true,
			imageTypeLimit = 1,
			enableTotalRecordCount = false,
			fields = ItemRepository.itemFields,
			enableImages = true
		)

		return object : HomeFragmentRow {
			override fun addToRowsAdapter(
				context: Context,
				cardPresenter: CardPresenter,
				rowsAdapter: MutableObjectAdapter<Row>
			) {
				val recentlyReleasedCardPresenter = createUniformCardPresenter()
				val rowDef = BrowseRowDef(
					context.getString(R.string.show_recently_released_row),
					recentlyReleasedQuery,
					getGenreItemLimit()
				)

				HomeFragmentBrowseRowDefRow(rowDef).addToRowsAdapter(
					context,
					recentlyReleasedCardPresenter,
					rowsAdapter
				)
			}
		}
	}

	private fun createWatchItAgainRow(): HomeFragmentRow {
		val currentUserId = requireNotNull(userRepository.currentUser.value?.id) {
			"User not available"
		}

		val watchItAgainQuery = GetItemsRequest(
			userId = currentUserId,
			includeItemTypes = listOf(BaseItemKind.MOVIE),
			filters = setOf(ItemFilter.IS_PLAYED),
			sortBy = setOf(ItemSortBy.DATE_CREATED),
			sortOrder = listOf(SortOrder.DESCENDING),
			limit = getGenreItemLimit(),
			recursive = true,
			imageTypeLimit = 1,
			enableTotalRecordCount = false,
			fields = ItemRepository.itemFields,
			enableImages = true
		)

		return object : HomeFragmentRow {
			override fun addToRowsAdapter(
				context: Context,
				cardPresenter: CardPresenter,
				rowsAdapter: MutableObjectAdapter<Row>
			) {
				val watchItAgainCardPresenter = createUniformCardPresenter()
				val rowDef = BrowseRowDef(
					context.getString(R.string.show_watch_it_again_row),
					watchItAgainQuery,
					getGenreItemLimit()
				)

				HomeFragmentBrowseRowDefRow(rowDef).addToRowsAdapter(
					context,
					watchItAgainCardPresenter,
					rowsAdapter
				)
			}
		}
	}

	private fun createEpisodeRow(): HomeFragmentRow {
		val currentUserId = userRepository.currentUser.value?.id

		val episodeQuery = GetItemsRequest(
			fields = ItemRepository.itemFields,
			includeItemTypes = setOf(BaseItemKind.EPISODE),
			filters = setOf(ItemFilter.IS_UNPLAYED),
			indexNumber = 1,
			recursive = true,
			isMissing = false,
			imageTypeLimit = 1,
			sortBy = setOf(ItemSortBy.PREMIERE_DATE),
			sortOrder = setOf(SortOrder.DESCENDING),
			enableTotalRecordCount = false,
			limit = getGenreItemLimit(),
		)

		return object : HomeFragmentRow {
			override fun addToRowsAdapter(
				context: Context,
				cardPresenter: CardPresenter,
				rowsAdapter: MutableObjectAdapter<Row>
			) {
				val useSeriesThumbnails = userPreferences[UserPreferences.seriesThumbnailsEnabled]
				val episodeCardPresenter = createthumbcardpresenter()
				val rowDef = BrowseRowDef(
					context.getString(R.string.show_recently_episodes_row),
					episodeQuery,
					getGenreItemLimit(),
					useSeriesThumbnails,  // Pass the preference here
					false
				)

				HomeFragmentBrowseRowDefRow(rowDef).addToRowsAdapter(
					context,
					episodeCardPresenter,
					rowsAdapter
				)
			}
		}
	}

	private fun createUniformCardPresenter(): CardPresenter {
		return object : CardPresenter(false, GENRE_CARD_HEIGHT) {
			init {
				setHomeScreen(true)
				setUniformAspect(true)
			}

			override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any?) {
				super.onBindViewHolder(viewHolder, item)

				(viewHolder.view as? LegacyImageCardView)?.let { cardView ->
					cardView.cardType = BaseCardView.CARD_TYPE_MAIN_ONLY
				}
			}
		}
	}
	private fun createthumbcardpresenter(): CardPresenter {
		return object : CardPresenter(false, GENRE_CARD_THUMB) {
			init {
				setHomeScreen(true)
				setUniformAspect(true)
			}

			override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any?) {
				super.onBindViewHolder(viewHolder, item)

				(viewHolder.view as? LegacyImageCardView)?.let { cardView ->
					cardView.setMainImageDimensions(200, 110)
					cardView.cardType = BaseCardView.CARD_TYPE_MAIN_ONLY
				}
			}
		}
	}
	private fun getGenreItemLimit(): Int {
		return userPreferences[UserPreferences.genreItemLimit]
	}
}

