// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.util.apiclient

@Deprecated("Utility class for callbacks used inside Java code. Do not use for new code.")
abstract class Response<T> {
	abstract fun onResponse(response: T)
	open fun onError(exception: Exception) = Unit
}

@Deprecated("Utility class for callbacks used inside Java code. Do not use for new code.")
abstract class EmptyResponse : Response<Unit>() {
	override fun onResponse(response: Unit) = onResponse()
	abstract fun onResponse()
}
