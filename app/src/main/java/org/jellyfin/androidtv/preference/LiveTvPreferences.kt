// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.preference

import org.jellyfin.androidtv.preference.store.DisplayPreferencesStore
import org.jellyfin.preference.booleanPreference
import org.jellyfin.preference.stringPreference
import org.jellyfin.sdk.api.client.ApiClient
import org.jellyfin.sdk.model.api.ItemSortBy

class LiveTvPreferences(
	api: ApiClient,
) : DisplayPreferencesStore(
	displayPreferencesId = "livetv",
	api = api,
) {
	companion object {
		val channelOrder = stringPreference("livetv-channelorder", ItemSortBy.DATE_PLAYED.name)
		val colorCodeGuide = booleanPreference("guide-colorcodedbackgrounds", false)
		val favsAtTop = booleanPreference("livetv-favoritechannelsattop", true)
		val showHDIndicator = booleanPreference("guide-indicator-hd", false)
		val showLiveIndicator = booleanPreference("guide-indicator-live", true)
		val showNewIndicator = booleanPreference("guide-indicator-new", false)
		val showPremiereIndicator = booleanPreference("guide-indicator-premiere", true)
		val showRepeatIndicator = booleanPreference("guide-indicator-repeat", false)
	}
}
