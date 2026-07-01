// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.ui.base.button

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class ButtonColors(
	val containerColor: Color,
	val contentColor: Color,
	val focusedContainerColor: Color,
	val focusedContentColor: Color,
	val disabledContainerColor: Color,
	val disabledContentColor: Color,
)
