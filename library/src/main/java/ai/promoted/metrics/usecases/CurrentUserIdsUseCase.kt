package ai.promoted.metrics.usecases

import ai.promoted.platform.KeyValueStorage

private const val KEY_USER_ID = "ai.promoted.user_id"
private const val KEY_LOG_USER_ID = "ai.promoted.log_user_id"

/**
 * Represents the dual use-case of both reading and writing the current value of the user IDs.
 *
 * This class will prefer in-memory storage unless reading from disk is required (i.e. upon app
 * launch). Updating the current value of a given user ID will result in both writing to the disk
 * (for future use) and to the in-memory reference (so as to avoid needing another read from disk).
 *
 * This class should be retained as a singleton in order to preserve the current user IDs across
 * other use cases, without the need for reading from disk every time.
 */
internal class CurrentUserIdsUseCase(
    private val keyValueStorage: KeyValueStorage
) {
    private var _lastUsedUserId: String? = null
    val currentUserId: String
        get() = when (val lastUsedUserId = _lastUsedUserId) {
            null -> {
                val lastStoredUserId = keyValueStorage.get(KEY_USER_ID, "")
                _lastUsedUserId = lastStoredUserId
                lastStoredUserId
            }
            else -> lastUsedUserId
        }

    private var _lastUsedLogUserId: String? = null
    val currentLogUserId: String
        get() = when (val lastUsedLogUserId = _lastUsedLogUserId) {
            null -> {
                val lastStoredLogUserId = keyValueStorage.get(KEY_LOG_USER_ID, "")
                _lastUsedLogUserId = lastStoredLogUserId
                lastStoredLogUserId
            }
            else -> lastUsedLogUserId
        }

    fun updateUserId(userId: String) {
        _lastUsedUserId = userId
        keyValueStorage.set(KEY_USER_ID, userId)
    }

    fun updateLogUserId(logUserId: String) {
        _lastUsedLogUserId = logUserId
        keyValueStorage.set(KEY_LOG_USER_ID, logUserId)
    }
}
