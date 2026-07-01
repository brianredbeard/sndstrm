// Original work: Copyright (C) 2014-2025 Jellyfin Contributors
// Modifications:  Copyright (C) 2025-2026 Sam42a (DUNE), sndstrm Contributors
// SPDX-License-Identifier: GPL-2.0-or-later
package org.jellyfin.preference

import kotlin.reflect.KClass

data class Preference<T : Any>(
	val key: String,
	val defaultValue: T,
	val type: KClass<T>
)

fun intPreference(key: String, defaultValue: Int) = Preference(key, defaultValue, Int::class)
fun longPreference(key: String, defaultValue: Long) = Preference(key, defaultValue, Long::class)
fun floatPreference(key: String, defaultValue: Float) = Preference(key, defaultValue, Float::class)
fun booleanPreference(key: String, defaultValue: Boolean) = Preference(key, defaultValue, Boolean::class)
fun stringPreference(key: String, defaultValue: String) = Preference(key, defaultValue, String::class)
fun <T : Any> enumPreference(key: String, defaultValue: T, type: KClass<T>) = Preference(key, defaultValue, type)

inline fun <reified T : Any> enumPreference(key: String, defaultValue: T) = enumPreference(key, defaultValue, T::class)
