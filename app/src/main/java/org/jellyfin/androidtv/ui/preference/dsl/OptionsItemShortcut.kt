// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.ui.preference.dsl

import android.content.Context
import androidx.annotation.StringRes
import androidx.preference.PreferenceCategory
import org.jellyfin.androidtv.ui.preference.custom.ButtonRemapPreference
import java.util.UUID

class OptionsItemShortcut(
	private val context: Context
) : OptionsItemMutable<Int>() {
	fun setTitle(@StringRes resId: Int) {
		title = context.getString(resId)
	}

	override fun build(category: PreferenceCategory, container: OptionsUpdateFunContainer) {
		val pref = ButtonRemapPreference(context).also {
			it.isPersistent = false
			it.key = UUID.randomUUID().toString()
			category.addPreference(it)
			it.isEnabled = dependencyCheckFun() && enabled
			it.isVisible = visible
			it.title = title
			it.dialogTitle = title
			it.summaryProvider = ButtonRemapPreference.ButtonRemapSummaryProvider.instance
			it.keyCode = binder.get()
			it.defaultKeyCode = binder.default()
			it.setOnPreferenceChangeListener { _, newValue ->
				binder.set(newValue as Int)

				container()

				// Always return false because we save it
				false
			}
		}

		container += {
			pref.isEnabled = dependencyCheckFun() && enabled
		}
	}
}

@OptionsDSL
fun OptionsCategory.shortcut(init: OptionsItemShortcut.() -> Unit) {
	this += OptionsItemShortcut(context).apply { init() }
}
