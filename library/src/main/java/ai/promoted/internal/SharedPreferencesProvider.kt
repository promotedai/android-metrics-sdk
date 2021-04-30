package ai.promoted.internal

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesProvider {
    private const val FILENAME = "ai.promoted.prefs"

    fun default(context: Context): SharedPreferences =
        context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)
}