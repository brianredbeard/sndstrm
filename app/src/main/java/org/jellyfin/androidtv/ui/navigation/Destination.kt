// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.ui.navigation

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import kotlin.reflect.KClass

sealed interface Destination {
	data class Fragment(
		val fragment: KClass<out androidx.fragment.app.Fragment>,
		val arguments: Bundle = bundleOf(),
	) : Destination
}

inline fun <reified T : Fragment> fragmentDestination(
	vararg arguments: Pair<String, Any?>,
) = Destination.Fragment(
	fragment = T::class,
	arguments = bundleOf(*arguments),
)
