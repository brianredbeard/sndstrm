// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.ui.base

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import org.jellyfin.androidtv.preference.constant.AppTheme

fun colorScheme(theme: AppTheme = AppTheme.DARK): ColorScheme = when (theme) {
	AppTheme.DARK -> darkColorScheme()
	AppTheme.EMERALD -> emeraldColorScheme()
	AppTheme.MUTED_PURPLE -> mutedPurpleColorScheme()
	AppTheme.PURPLE_HAZE -> purpleHazeColorScheme()
	AppTheme.DARK_PURPLE -> darkPurpleColorScheme()
	AppTheme.FLEXY -> flexyColorScheme()
	AppTheme.YELLOW_TOWN -> yellowTownColorScheme()
	AppTheme.BASIC -> darkColorScheme()
}

private fun darkColorScheme() = ColorScheme(
	background = Color(0xFF101010),
	onBackground = Color(0xFFFFFFFF),
	button = Color(0xB3747474),
	onButton = Color(0xFFDDDDDD),
	buttonFocused = Color(0xE6CCCCCC),
	onButtonFocused = Color(0xFF444444),
	buttonDisabled = Color(0x33747474),
	onButtonDisabled = Color(0xFF686868),
	buttonActive = Color(0x4DCCCCCC),
	onButtonActive = Color(0xFFDDDDDD),
	input = Color(0xB3747474),
	onInput = Color(0xE6CCCCCC),
	inputFocused = Color(0xE6CCCCCC),
	onInputFocused = Color(0xFFDDDDDD),
	recording = Color(0xB3FF7474),
	onRecording = Color(0xFFDDDDDD),
	popover = Color(0xFF212225),
)

private fun emeraldColorScheme() = ColorScheme(
	background = Color(0xFF0b4111),
	onBackground = Color(0xFFFFFFFF),
	button = Color(0xB3166D1B),
	onButton = Color(0xFFDDDDDD),
	buttonFocused = Color(0xE64CAF50),
	onButtonFocused = Color(0xFF1B1B1B),
	buttonDisabled = Color(0x33166D1B),
	onButtonDisabled = Color(0xFF688F6A),
	buttonActive = Color(0x4D4CAF50),
	onButtonActive = Color(0xFFDDDDDD),
	input = Color(0xB3166D1B),
	onInput = Color(0xE64CAF50),
	inputFocused = Color(0xE64CAF50),
	onInputFocused = Color(0xFFDDDDDD),
	recording = Color(0xB3FF7474),
	onRecording = Color(0xFFDDDDDD),
	popover = Color(0xFF0D2E10),
)

private fun mutedPurpleColorScheme() = ColorScheme(
	background = Color(0xFF1A1A2E),
	onBackground = Color(0xFFE0E0E0),
	button = Color(0xB3393950),
	onButton = Color(0xFFCCCCCC),
	buttonFocused = Color(0xE6A78BFA),
	onButtonFocused = Color(0xFF1A1A2E),
	buttonDisabled = Color(0x33393950),
	onButtonDisabled = Color(0xFF686880),
	buttonActive = Color(0x4DA78BFA),
	onButtonActive = Color(0xFFDDDDDD),
	input = Color(0xB3393950),
	onInput = Color(0xE6A78BFA),
	inputFocused = Color(0xE6A78BFA),
	onInputFocused = Color(0xFFDDDDDD),
	recording = Color(0xB3FF7474),
	onRecording = Color(0xFFDDDDDD),
	popover = Color(0xFF16162B),
)

private fun purpleHazeColorScheme() = ColorScheme(
	background = Color(0xFF0D0221),
	onBackground = Color(0xFFE8D5F5),
	button = Color(0xB3362157),
	onButton = Color(0xFFD4B8E8),
	buttonFocused = Color(0xE6B388FF),
	onButtonFocused = Color(0xFF1A0A30),
	buttonDisabled = Color(0x33362157),
	onButtonDisabled = Color(0xFF6B5080),
	buttonActive = Color(0x4DB388FF),
	onButtonActive = Color(0xFFE8D5F5),
	input = Color(0xB3362157),
	onInput = Color(0xE6B388FF),
	inputFocused = Color(0xE6B388FF),
	onInputFocused = Color(0xFFE8D5F5),
	recording = Color(0xB3FF7474),
	onRecording = Color(0xFFDDDDDD),
	popover = Color(0xFF150535),
)

private fun darkPurpleColorScheme() = ColorScheme(
	background = Color(0xFF15041F),
	onBackground = Color(0xFFE0D0F0),
	button = Color(0xB32D1045),
	onButton = Color(0xFFD0C0E0),
	buttonFocused = Color(0xE69B59D4),
	onButtonFocused = Color(0xFF15041F),
	buttonDisabled = Color(0x332D1045),
	onButtonDisabled = Color(0xFF604080),
	buttonActive = Color(0x4D9B59D4),
	onButtonActive = Color(0xFFE0D0F0),
	input = Color(0xB32D1045),
	onInput = Color(0xE69B59D4),
	inputFocused = Color(0xE69B59D4),
	onInputFocused = Color(0xFFE0D0F0),
	recording = Color(0xB3FF7474),
	onRecording = Color(0xFFDDDDDD),
	popover = Color(0xFF1A0828),
)

private fun flexyColorScheme() = ColorScheme(
	background = Color(0xFF141414),
	onBackground = Color(0xFFFFFFFF),
	button = Color(0xB3333333),
	onButton = Color(0xFFDDDDDD),
	buttonFocused = Color(0xE6E50914),
	onButtonFocused = Color(0xFFFFFFFF),
	buttonDisabled = Color(0x33333333),
	onButtonDisabled = Color(0xFF686868),
	buttonActive = Color(0x4DE50914),
	onButtonActive = Color(0xFFFFFFFF),
	input = Color(0xB3333333),
	onInput = Color(0xE6E50914),
	inputFocused = Color(0xE6E50914),
	onInputFocused = Color(0xFFFFFFFF),
	recording = Color(0xB3FF7474),
	onRecording = Color(0xFFDDDDDD),
	popover = Color(0xFF1C1C1C),
)

private fun yellowTownColorScheme() = ColorScheme(
	background = Color(0xFF282a2d),
	onBackground = Color(0xFFFFFFFF),
	button = Color(0xB3403D00),
	onButton = Color(0xFFDDDDDD),
	buttonFocused = Color(0xE6E5A00D),
	onButtonFocused = Color(0xFF1A1800),
	buttonDisabled = Color(0x33403D00),
	onButtonDisabled = Color(0xFF8A8560),
	buttonActive = Color(0x4DE5A00D),
	onButtonActive = Color(0xFFFFFFFF),
	input = Color(0xB3403D00),
	onInput = Color(0xE6E5A00D),
	inputFocused = Color(0xE6E5A00D),
	onInputFocused = Color(0xFFFFFFFF),
	recording = Color(0xB3FF7474),
	onRecording = Color(0xFFDDDDDD),
	popover = Color(0xFF2E2C1A),
)

@Immutable
data class ColorScheme(
	val background: Color,
	val onBackground: Color,

	val button: Color,
	val onButton: Color,
	val buttonFocused: Color,
	val onButtonFocused: Color,
	val buttonDisabled: Color,
	val onButtonDisabled: Color,
	val buttonActive: Color,
	val onButtonActive: Color,

	val input: Color,
	val onInput: Color,
	val inputFocused: Color,
	val onInputFocused: Color,

	val recording: Color,
	val onRecording: Color,

	val popover: Color,
)

val LocalColorScheme = staticCompositionLocalOf { colorScheme() }
