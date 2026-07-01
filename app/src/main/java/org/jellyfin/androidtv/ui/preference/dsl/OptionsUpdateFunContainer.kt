// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.ui.preference.dsl

typealias OptionsUpdateFun = () -> Unit

class OptionsUpdateFunContainer {
	private val callbacks = mutableSetOf<OptionsUpdateFun>()

	operator fun plusAssign(callback: OptionsUpdateFun) {
		callbacks += callback
	}

	operator fun invoke() {
		callbacks.forEach {
			it.invoke()
		}
	}
}
