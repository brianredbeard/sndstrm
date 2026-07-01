// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.ui.presentation

import androidx.annotation.NonNull
import androidx.leanback.widget.RowHeaderPresenter

class CustomRowHeaderPresenter : RowHeaderPresenter() {
    @Suppress("UNUSED_PARAMETER")
    override fun onSelectLevelChanged(holder: ViewHolder) {
        // No action needed
    }
}
