package ai.promoted.metrics.usecases

import ai.promoted.metrics.storage.UserIdStorage

/**
 * Represents the dual use-case of both reading and writing the current value of the user IDs.
 *
 * This class will prefer in-memory storage unless reading from disk is required (i.e. upon app
 * launch). Updating the current value of a given user ID will result in both writing to the disk
 * (for future use) and to the in-memory reference (so as to avoid needing another read from disk).
 */
internal class CurrentUserIdsUseCase(private val idStorage: UserIdStorage) {
    private var _lastUsedUserId: String? = null
    val currentUserId: String
        get() = when (val lastUsedUserId = _lastUsedUserId) {
            null -> {
                val lastStoredUserId = idStorage.userId
                _lastUsedUserId = lastStoredUserId
                lastStoredUserId
            }
            else -> lastUsedUserId
        }

    private var _lastUsedLogUserId: String? = null
    val currentLogUserId: String
        get() = when (val lastUsedLogUserId = _lastUsedLogUserId) {
            null -> {
                val lastStoredLogUserId = idStorage.logUserId
                _lastUsedLogUserId = lastStoredLogUserId
                lastStoredLogUserId
            }
            else -> lastUsedLogUserId
        }

    fun updateUserId(userId: String) {
        _lastUsedUserId = userId
        idStorage.userId = userId
    }

    fun updateLogUserId(logUserId: String) {
        _lastUsedLogUserId = logUserId
        idStorage.logUserId = logUserId
    }
}
