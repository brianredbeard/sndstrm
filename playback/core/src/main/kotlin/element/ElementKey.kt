// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.playback.core.element

/**
 * A key to identify the type of an element.
 */
class ElementKey<T : Any>(val name: String) {
	override fun toString(): String = "ElementKey $name"
}
