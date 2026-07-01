// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.ui.preference.dsl

import android.content.Context
import androidx.annotation.StringRes
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceCategory
import java.util.UUID

class OptionsItemInfo(
	private val context: Context
) : OptionsItem {
	var title: String? = null
	var content: String? = null

	fun setTitle(@StringRes resId: Int) {
		title = context.getString(resId)
	}

	fun setContent(@StringRes resId: Int) {
		content = context.getString(resId)
	}

	override fun build(category: PreferenceCategory, container: OptionsUpdateFunContainer) {
		EditTextPreference(context).also {
			it.isPersistent = false
			it.key = UUID.randomUUID().toString()
			category.addPreference(it)
			it.isEnabled = false
			it.title = title
			it.summary = content
		}
	}
}

@OptionsDSL
fun OptionsCategory.info(init: OptionsItemInfo.() -> Unit) {
	this += OptionsItemInfo(context).apply { init() }
}
