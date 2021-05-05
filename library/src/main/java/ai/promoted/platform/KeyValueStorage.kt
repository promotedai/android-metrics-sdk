package ai.promoted.platform

import android.content.SharedPreferences

/**
 * Abstraction to allow for DI/testability. Represents some platform-provided, simple
 * of key/value storage of strings.
 */
internal interface KeyValueStorage {
    fun set(key: String, value: String)
    fun get(key: String, defaultValue: String): String
}

internal class SharedPrefsKeyValueStorage(
    private val sharedPreferences: SharedPreferences
) : KeyValueStorage {
    private val editor: SharedPreferences.Editor
        get() = sharedPreferences.edit()

    override fun set(key: String, value: String) = editor.putString(key, value).apply()
    override fun get(key: String, defaultValue: String): String =
        sharedPreferences.getString(key, defaultValue) ?: defaultValue
}
