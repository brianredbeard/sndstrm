// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.util.sdk

import org.jellyfin.sdk.api.client.ApiClient


/**
 * Check if the [baseUrl] and [accessToken] are not null.
 */
val ApiClient.isUsable
	get() = baseUrl != null && accessToken != null
