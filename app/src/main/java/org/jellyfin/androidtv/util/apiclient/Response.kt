// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.util.apiclient

import androidx.lifecycle.Lifecycle

@Deprecated("Utility class for callbacks used inside Java code. Do not use for new code.")
abstract class Response<T>(private val lifecycle: Lifecycle? = null) {
	val isActive get() = lifecycle?.currentState?.isAtLeast(Lifecycle.State.STARTED) ?: true

	abstract fun onResponse(response: T)
	open fun onError(exception: Exception) = Unit
}

@Deprecated("Utility class for callbacks used inside Java code. Do not use for new code.")
abstract class EmptyResponse(lifecycle: Lifecycle) : Response<Unit>(lifecycle) {
	override fun onResponse(response: Unit) = onResponse()
	abstract fun onResponse()
}
