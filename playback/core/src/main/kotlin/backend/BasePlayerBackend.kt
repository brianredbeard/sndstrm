// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.playback.core.backend

/**
 * A base class that implements the event listening part of [PlayerBackend].
 */
abstract class BasePlayerBackend : PlayerBackend {
	private var _listener: PlayerBackendEventListener? = null
	protected val listener: PlayerBackendEventListener? get() = _listener

	override fun setListener(eventListener: PlayerBackendEventListener?) {
		_listener = eventListener
	}
}
