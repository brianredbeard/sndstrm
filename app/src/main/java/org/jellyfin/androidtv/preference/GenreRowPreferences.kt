// Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.preference

import org.jellyfin.preference.stringPreference
import org.jellyfin.sdk.api.client.ApiClient
import org.jellyfin.androidtv.preference.store.DisplayPreferencesStore

class GenreRowPreferences(
    api: ApiClient
) : DisplayPreferencesStore(
    displayPreferencesId = "genererowsettings",
    api = api,
    app = "emby",
) {
    companion object {
        val homeGenreMovie = stringPreference("homeGenreMovie", "")
        val homeGenreTvShow = stringPreference("homeGenreTvShow", "")
        val homeGenreActionAdventure = stringPreference("homeGenreActionAdventure", "")
        val homeGenreFavorites = stringPreference("homeGenreFavorites", "")
        val homeGenreSuggestedMovies = stringPreference("homeGenreSuggestedMovies", "show")
        val homeGenreSuggestedTvShows = stringPreference("homeGenreSuggestedTvShows", "show")
    }

    val homeGenreMoviePref = homeGenreMovie
    val homeGenreTvShowPref = homeGenreTvShow
    val homeGenreActionAdventurePref = homeGenreActionAdventure
    val homeGenreFavoritesPref = homeGenreFavorites
    val homeGenreSuggestedMoviesPref = homeGenreSuggestedMovies
    val homeGenreSuggestedTvShowsPref = homeGenreSuggestedTvShows

    fun getHomeGenreMovie(): String = get(homeGenreMovie)
    fun getHomeGenreTvShow(): String = get(homeGenreTvShow)
    fun getHomeGenreActionAdventure(): String = get(homeGenreActionAdventure)
    fun getHomeGenreFavorites(): String = get(homeGenreFavorites)
    fun getHomeGenreSuggestedMovies(): String = get(homeGenreSuggestedMovies)
    fun getHomeGenreSuggestedTvShows(): String = get(homeGenreSuggestedTvShows)
}