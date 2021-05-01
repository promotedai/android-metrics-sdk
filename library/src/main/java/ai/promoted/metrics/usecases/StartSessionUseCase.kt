package ai.promoted.metrics.usecases

import ai.promoted.metrics.id.IdGenerator

internal class StartSessionUseCase(
    private val idGenerator: IdGenerator,
    private val idStorageUseCase: IdStorageUseCase,
    private val logUserUseCase: LogUserUseCase,
    private val logSessionUseCase: LogSessionUseCase
) {
    private var shouldAdvanceSessionId = false
    var sessionId: String = idGenerator.newId()
        private set

    fun startSession(userId: String) {
        // Typically, we would generate a new session ID every time startSession is called;
        // however, there are cases where metrics need to be logged prior to a session starting.
        // For this case, we allow the initial value of sessionId to be retained on the first
        // startSession() call, so that metrics between the pre-session and session can be
        // properly associated
        if (!shouldAdvanceSessionId) shouldAdvanceSessionId = true
        else sessionId = idGenerator.newId()

        syncCurrentUserId(userId)
        logUserUseCase.logUser()
        logSessionUseCase.logSession()
    }

    private fun syncCurrentUserId(userId: String) {
        // if it's a new user ID (different value than what's in the store), store it locally & persistence
        // Now that a new user ID exists, a new logUserId needs to be generated & then stored
        if (idStorageUseCase.currentUserId != userId) {
            idStorageUseCase.updateUserId(userId)
            idStorageUseCase.updateLogUserId(idGenerator.newId())
        }
    }
}