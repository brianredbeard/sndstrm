// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.ui.preference

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import org.jellyfin.androidtv.R
import org.jellyfin.androidtv.ui.preference.screen.UserPreferencesScreen

class PreferencesActivity : FragmentActivity(R.layout.fragment_content_view) {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val screen = intent.extras?.getString(EXTRA_SCREEN) ?: UserPreferencesScreen::class.qualifiedName
		val screenArgs = intent.extras?.getBundle(EXTRA_SCREEN_ARGS) ?: bundleOf()

		supportFragmentManager
			.beginTransaction()
			.replace(R.id.content_view, PreferencesFragment().apply {
				// Set screen
				arguments = bundleOf(
					PreferencesFragment.EXTRA_SCREEN to screen,
					PreferencesFragment.EXTRA_SCREEN_ARGS to screenArgs
				)
			}, FRAGMENT_TAG)
			.commit()
	}

	companion object {
		const val EXTRA_SCREEN = "screen"
		const val EXTRA_SCREEN_ARGS = "screen_args"
		const val FRAGMENT_TAG = "PreferencesActivity"
	}
}

