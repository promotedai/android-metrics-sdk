package ai.promoted.metrics.usecases

import ai.promoted.metrics.MetricsLogger
import ai.promoted.metrics.id.AncestorId
import ai.promoted.metrics.id.IdGenerator
import ai.promoted.platform.Clock
import ai.promoted.xray.Xray

internal class TrackUserUseCase(
    idGenerator: IdGenerator,
    private val clock: Clock,
    private val logger: MetricsLogger,
    private val currentUserIdsUseCase: CurrentUserIdsUseCase,
    private val xray: Xray
) {
    private val logUserAncestorId = AncestorId(idGenerator)

    val currentOrNullUserId: String?
        get() {
            val currentUserId = currentUserIdsUseCase.currentUserId
            return if (currentUserId.isEmpty()) null
            else currentUserId
        }

    val currentLogUserId: String
        get() = currentUserIdsUseCase.currentLogUserId

    val currentOrPendingLogUserId: String
        get() {
            val currentLogUserId = this.currentLogUserId
            return if (currentLogUserId.isEmpty()) logUserAncestorId.currentOrPendingValue
            else currentLogUserId
        }

    val currentOrNullLogUserId: String?
        get() {
            val currentLogUserId = this.currentLogUserId
            return if (currentLogUserId.isEmpty()) null
            else currentLogUserId
        }

    fun setUserId(userId: String) = xray.monitored {
        // if it's a new user ID (different value than what's in the store), store it in memory
        // & persist it.
        if (currentUserIdsUseCase.currentUserId != userId) {
            currentUserIdsUseCase.updateUserId(userId)

            // Now that a new user ID exists, a new logUserId needs to be generated & then stored
            logUserAncestorId.advance()
            currentUserIdsUseCase.updateLogUserId(logUserAncestorId.currentValue)

            logUser()
        }
    }

    fun overrideLogUserId(logUserId: String) = xray.monitored {
        currentUserIdsUseCase.updateUserId("")
        currentUserIdsUseCase.updateLogUserId(logUserId)
        logUser()
    }

    private fun logUser() = logger.enqueueMessage(createUserMessage(clock))
}
