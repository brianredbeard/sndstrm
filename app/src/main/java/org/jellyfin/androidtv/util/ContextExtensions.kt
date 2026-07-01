// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.util

import android.app.Activity
import android.app.UiModeManager
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import androidx.annotation.PluralsRes
import androidx.core.content.getSystemService

/**
 * Get the activity hosting the current context
 */
tailrec fun Context.getActivity(): Activity? = when (this) {
	is Activity -> this
	else -> (this as? ContextWrapper)?.baseContext?.getActivity()
}

/**
 * Wrapper for getQuantityString in [resources] that supports all number types.
 * Note: Adds the [quantity] as first vararg, this is different behavior compared
 * to the platform function!
 */
fun Context.getQuantityString(@PluralsRes id: Int, quantity: Number, vararg args: Any) =
	resources.getQuantityString(id, quantity.toInt(), quantity, *args)

fun Context.isTvDevice(): Boolean {
	val uiModeManager = getSystemService<UiModeManager>()
	val supportedUiModes = setOf(Configuration.UI_MODE_TYPE_TELEVISION, Configuration.UI_MODE_TYPE_UNDEFINED)

	return supportedUiModes.contains(uiModeManager?.currentModeType) or
		packageManager.hasSystemFeature("android.hardware.hdmi.cec") or
		!packageManager.hasSystemFeature("android.hardware.touchscreen")
}
