// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.util.sdk

import org.jellyfin.sdk.model.DeviceInfo
import org.jellyfin.sdk.model.UUID
import java.security.MessageDigest

fun DeviceInfo.forUser(userId: UUID) = forUser(userId.toString())

/**
 * @param user User ID or name
 */
fun DeviceInfo.forUser(user: String): DeviceInfo = copy(
	id = MessageDigest.getInstance("SHA-1").run {
		update("${id}+$user".toByteArray())
		digest().joinToString("") { "%02x".format(it) }
	},
)
