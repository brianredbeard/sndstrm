// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.auth.model

import org.jellyfin.sdk.discovery.RecommendedServerIssue
import org.jellyfin.sdk.model.api.PublicSystemInfo
import java.util.UUID

sealed class ServerAdditionState
data class ConnectingState(val address: String) : ServerAdditionState()
data class UnableToConnectState(val addressCandidates: Map<String, Collection<RecommendedServerIssue>>) : ServerAdditionState()
data class ConnectedState(val id: UUID, val publicInfo: PublicSystemInfo) : ServerAdditionState()
