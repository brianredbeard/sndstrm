// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.ui.preference.dsl

data class OptionsBinder<T>(
	val get: () -> T,
	val set: (value: T) -> Unit,
	val default: () -> T
) {
	class Builder<T> {
		private var getFun: (() -> T)? = null
		private var setFun: ((value: T) -> Unit)? = null
		private var defaultFun: (() -> T)? = null

		fun get(getFun: () -> T) {
			this.getFun = getFun
		}

		fun default(defaultFun: () -> T) {
			this.defaultFun = defaultFun
		}

		fun set(setFun: (value: T) -> Unit) {
			this.setFun = setFun
		}

		fun build(): OptionsBinder<T> = OptionsBinder(
			getFun!!,
			setFun!!,
			defaultFun!!
		)
	}
}
