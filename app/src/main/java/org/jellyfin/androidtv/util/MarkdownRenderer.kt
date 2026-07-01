// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.util

import android.content.Context
import android.text.Spanned
import io.noties.markwon.Markwon
import io.noties.markwon.html.HtmlPlugin

class MarkdownRenderer(context: Context) {
	private val markwon = Markwon.builder(context)
		.usePlugin(HtmlPlugin.create())
		.build()

	/**
	 * Convert string with markdown and HTML to a [Spanned].
	 */
	fun toMarkdownSpanned(input: String): Spanned = markwon.toMarkdown(input)
}
