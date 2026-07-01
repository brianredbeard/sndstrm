// Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class InstallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_PACKAGE_ADDED -> {
                val packageName = intent.data?.schemeSpecificPart
                Log.d("InstallReceiver", "Package installed: $packageName")
            }
            Intent.ACTION_PACKAGE_REPLACED -> {
                val packageName = intent.data?.schemeSpecificPart
                Log.d("InstallReceiver", "Package replaced: $packageName")
            }
            Intent.ACTION_INSTALL_PACKAGE -> {
                Log.d("InstallReceiver", "Install package intent received")
            }
        }
    }
}
