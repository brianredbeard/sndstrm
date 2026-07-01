// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.auth.model

sealed class AuthenticateMethod
data class AutomaticAuthenticateMethod(val user: User) : AuthenticateMethod()
data class CredentialAuthenticateMethod(val username: String, val password: String = "") : AuthenticateMethod()
data class QuickConnectAuthenticateMethod(val secret: String) : AuthenticateMethod()
