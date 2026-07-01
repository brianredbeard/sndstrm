// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.util.speech

import androidx.compose.runtime.Stable

@Stable
sealed interface SpeechRecognizerStatus {
	data object Idle : SpeechRecognizerStatus

	data class Listening(val hasSpeech: Boolean) : SpeechRecognizerStatus

	data object RequestingPermission : SpeechRecognizerStatus
	data class PermissionDenied(val canRequest: Boolean) : SpeechRecognizerStatus

	data object Unavailable : SpeechRecognizerStatus
	data class Error(val code: Int) : SpeechRecognizerStatus
}
