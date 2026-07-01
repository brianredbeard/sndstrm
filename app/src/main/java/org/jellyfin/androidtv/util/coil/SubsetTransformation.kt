// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.androidtv.util.coil

import android.graphics.Bitmap
import coil3.size.Size
import coil3.transform.Transformation

class SubsetTransformation(
	private val x: Int,
	private val y: Int,
	private val width: Int,
	private val height: Int,
) : Transformation() {
	override val cacheKey: String = "$x,$y,$width,$height"

	override suspend fun transform(
		input: Bitmap,
		size: Size,
	): Bitmap = Bitmap.createBitmap(input, x, y, width, height)
}
