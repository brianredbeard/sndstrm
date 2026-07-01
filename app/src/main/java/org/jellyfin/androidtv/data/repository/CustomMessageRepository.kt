// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.jellyfin.androidtv.constant.CustomMessage

interface CustomMessageRepository {
	val message: StateFlow<CustomMessage?>
	fun pushMessage(message: CustomMessage)
}

class CustomMessageRepositoryImpl : CustomMessageRepository {
	private val _message = MutableStateFlow<CustomMessage?>(null)
	override val message get() = _message.asStateFlow()

	override fun pushMessage(message: CustomMessage) {
		// Make sure to re-emit the same message if requested
		if (_message.value == message) _message.value = null

		_message.value = message
	}
}
