package ai.promoted.metrics.usecases

import ai.promoted.metrics.storage.UserIdStorage

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