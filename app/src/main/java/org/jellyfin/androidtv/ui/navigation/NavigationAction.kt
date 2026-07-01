// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.ui.navigation

/**
 * Emitted actions from the navigation repository.
 */
sealed interface NavigationAction {
    /**
     * Navigate to the fragment in [destination].
     */
    data class NavigateFragment(
        val destination: Destination.Fragment,
        val addToBackStack: Boolean,
        val replace: Boolean,
        val clear: Boolean,
    ) : NavigationAction

    /**
     * Go back to the previous fragment manager state.
     */
    object GoBack : NavigationAction

    /**
     * Do nothing.
     */
    object Nothing : NavigationAction

    companion object {
        /**
         * Creates a new [NavigateFragment] instance.
         */
        fun navigateFragment(
            destination: Destination.Fragment,
            addToBackStack: Boolean = true,
            replace: Boolean = false,
            clear: Boolean = false
        ): NavigationAction = NavigateFragment(destination, addToBackStack, replace, clear)
    }
}
