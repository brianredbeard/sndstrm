// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.auth.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.jellyfin.sdk.model.api.UserDto

/**
 * Repository to get the current authenticated user.
 */
interface UserRepository {
	val currentUser: StateFlow<UserDto?>

	fun updateCurrentUser(user: UserDto?)
}

class UserRepositoryImpl : UserRepository {
	override val currentUser = MutableStateFlow<UserDto?>(null)

	override fun updateCurrentUser(user: UserDto?) {
		currentUser.value = user
	}
}
