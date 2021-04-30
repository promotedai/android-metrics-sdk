package ai.promoted.metrics.storage

import android.content.SharedPreferences

private const val KEY_USER_ID = "ai.promoted.user_id"
private const val KEY_LOG_USER_ID = "ai.promoted.log_user_id"

internal class PrefsIdStorage(
    private val sharedPreferences: SharedPreferences
) : IdStorage {
    private val editor: SharedPreferences.Editor
        get() = sharedPreferences.edit()

    override var userId: String
        get() = sharedPreferences.getString(KEY_USER_ID, null) ?: ""
        set(value) = editor.putString(KEY_USER_ID, value).apply()

    override var logUserId: String
        get() = sharedPreferences.getString(KEY_LOG_USER_ID, null) ?: ""
        set(value) = editor.putString(KEY_LOG_USER_ID, value).apply()
}