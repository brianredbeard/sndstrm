// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.ui.playback

import org.jellyfin.sdk.model.api.BaseItemDto

class VideoQueueManager {
	private var _currentVideoQueue: List<BaseItemDto> = emptyList()
	private var _currentMediaPosition = -1
	private var _lastPlayedAudioLanguageIsoCode: String? = null

	fun setCurrentVideoQueue(items: List<BaseItemDto>?) {
		if (items.isNullOrEmpty()) return clearVideoQueue()

		_currentVideoQueue = items.toMutableList()
		_currentMediaPosition = 0
	}

	fun getCurrentVideoQueue(): List<BaseItemDto> = _currentVideoQueue

	fun setCurrentMediaPosition(currentMediaPosition: Int) {
		if (currentMediaPosition !in 0.._currentVideoQueue.size) return

		_currentMediaPosition = currentMediaPosition
	}

	fun getCurrentMediaPosition() = _currentMediaPosition

	fun getLastPlayedAudioLanguageIsoCode(): String? {
		return _lastPlayedAudioLanguageIsoCode
	}

	fun setLastPlayedAudioLanguageIsoCode(isoCode: String?) {
		_lastPlayedAudioLanguageIsoCode = isoCode
	}

	fun clearVideoQueue() {
		_currentVideoQueue = emptyList()
		_currentMediaPosition = -1
		_lastPlayedAudioLanguageIsoCode = null
	}
}
