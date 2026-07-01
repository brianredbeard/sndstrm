// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.ui.base

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle

object TypographyDefaults {
	val Default: TextStyle = TextStyle.Default
}

@Immutable
data class Typography(
	val default: TextStyle = TypographyDefaults.Default,
)

val LocalTypography = staticCompositionLocalOf { Typography() }
