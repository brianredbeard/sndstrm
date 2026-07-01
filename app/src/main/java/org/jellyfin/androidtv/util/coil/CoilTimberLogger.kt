// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.util.coil

import android.util.Log
import coil3.util.Logger
import timber.log.Timber

class CoilTimberLogger(
	override var minLevel: Logger.Level = Logger.Level.Debug,
) : Logger {
	override fun log(tag: String, level: Logger.Level, message: String?, throwable: Throwable?) {
		val priority = when (level) {
			Logger.Level.Verbose -> Log.VERBOSE
			Logger.Level.Debug -> Log.DEBUG
			Logger.Level.Info -> Log.INFO
			Logger.Level.Warn -> Log.WARN
			Logger.Level.Error -> Log.ERROR
		}

		Timber.tag("CoilTimberLogger.$tag").log(priority, throwable, message)
	}
}
