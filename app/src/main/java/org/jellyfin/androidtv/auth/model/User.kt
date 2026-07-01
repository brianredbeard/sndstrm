// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.auth.model

import java.util.UUID

/**
 * User model used locally.
 */
sealed class User {
	abstract val id: UUID
	abstract val serverId: UUID
	abstract val name: String
	abstract val accessToken: String?
	abstract val imageTag: String?

	abstract fun withToken(accessToken: String): User

	override fun equals(other: Any?) = other is User
		&& serverId == other.serverId
		&& id == other.id

	override fun hashCode(): Int {
		var result = id.hashCode()
		result = 31 * result + serverId.hashCode()
		return result
	}
}

/**
 * Represents a user stored client side.
 */
data class PrivateUser(
	override val id: UUID,
	override val serverId: UUID,
	override val name: String,
	override val accessToken: String?,
	override val imageTag: String?,
	val lastUsed: Long,
) : User() {
	override fun withToken(accessToken: String) = copy(accessToken = accessToken)
}

/**
 * Represents a user stored server side. Found using the Public User endpoint.
 */
data class PublicUser(
	override val id: UUID,
	override val serverId: UUID,
	override val name: String,
	override val accessToken: String?,
	override val imageTag: String?,
) : User() {
	override fun withToken(accessToken: String) = copy(accessToken = accessToken)
}
