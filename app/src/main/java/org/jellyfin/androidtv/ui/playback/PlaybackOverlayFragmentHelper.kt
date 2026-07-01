// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.ui.playback

import org.jellyfin.androidtv.ui.ScreensaverViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class PlaybackOverlayFragmentHelper(
	val fragment: CustomPlaybackOverlayFragment
) {
	private val screensaverViewModel by fragment.activityViewModel<ScreensaverViewModel>()
	private var screensaverLock: (() -> Unit)? = null

	fun setScreensaverLock(enabled: Boolean) {
		if (enabled && screensaverLock == null) {
			screensaverLock = screensaverViewModel.addLifecycleLock(fragment.lifecycle)
		} else if (!enabled) {
			screensaverLock?.invoke()
			screensaverLock = null
		}
	}
}
