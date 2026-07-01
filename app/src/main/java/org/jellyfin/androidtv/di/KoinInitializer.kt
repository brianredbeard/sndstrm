// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.di

import android.content.Context
import androidx.startup.Initializer
import org.jellyfin.androidtv.LogInitializer
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

class KoinInitializer : Initializer<KoinApplication> {
	override fun create(context: Context): KoinApplication = startKoin {
		androidContext(context)

		modules(
			androidModule,
			appModule,
			authModule,
			playbackModule,
			preferenceModule,
			utilsModule,
		)
	}

	override fun dependencies() = listOf(LogInitializer::class.java)
}

