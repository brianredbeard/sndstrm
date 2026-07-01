// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.di

import android.content.Context
import org.jellyfin.androidtv.preference.UserPreferences
import org.jellyfin.androidtv.util.ImageHelper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val utilsModule = module {
	single { ImageHelper(get(), get(), androidContext()) }
}
