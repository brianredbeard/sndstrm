// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.auth.model

import org.jellyfin.sdk.api.client.exception.ApiClientException

sealed class LoginState
data object AuthenticatingState : LoginState()
data object RequireSignInState : LoginState()
data object ServerUnavailableState : LoginState()
data class ServerVersionNotSupported(val server: Server) : LoginState()
data class ApiClientErrorLoginState(val error: ApiClientException) : LoginState()
data object AuthenticatedState : LoginState()
