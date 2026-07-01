// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.util

import android.content.Context
import android.os.Build
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@Suppress("DEPRECATION")
val Context.locale: Locale
	get() = when {
		Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> resources.configuration.getLocales().get(0)
		else -> resources.configuration.locale
	}

@JvmOverloads
fun Context.getDateFormatter(
	style: FormatStyle = FormatStyle.SHORT
): DateTimeFormatter = DateTimeFormatter
	.ofLocalizedDateTime(style)
	.withLocale(locale)

@JvmOverloads
fun Context.getTimeFormatter(
	style: FormatStyle = FormatStyle.SHORT
): DateTimeFormatter = DateTimeFormatter
	.ofLocalizedTime(style)
	.withLocale(locale)
