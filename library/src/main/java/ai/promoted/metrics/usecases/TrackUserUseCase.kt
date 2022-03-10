package ai.promoted.metrics.usecases

import ai.promoted.metrics.MetricsLogger
import ai.promoted.metrics.id.AncestorId
import ai.promoted.metrics.id.IdGenerator
import ai.promoted.platform.Clock
import ai.promoted.xray.Xray

internal class TrackUserUseCase(
    idGenerator: IdGenerator,
    private val clock: Clock,
    private val currentUserIdsUseCase: CurrentUserIdsUseCase,
    private val xray: Xray
) {
    private val logUserAncestorId = AncestorId(idGenerator)

    val currentOrNullUserId: String?
        get() {
            val currentUserId = currentUserIdsUseCase.currentUserId
            return currentUserId.ifEmpty { null }
        }

    val currentLogUserId: String
        get() = currentUserIdsUseCase.currentLogUserId

    val currentOrPendingLogUserId: String
        get() {
            val currentLogUserId = this.currentLogUserId
            return currentLogUserId.ifEmpty { logUserAncestorId.currentOrPendingValue }
        }

    val currentOrNullLogUserId: String?
        get() {
            val currentLogUserId = this.currentLogUserId
            return currentLogUserId.ifEmpty { null }
        }

    fun setUserId(logger: MetricsLogger, userId: String) = xray.monitored {
        // if it's a new user ID (different value than what's in the store), store it in memory
        // & persist it.
        if (currentUserIdsUseCase.currentUserId != userId) {
            currentUserIdsUseCase.updateUserId(userId)

            // Now that a new user ID exists, a new logUserId needs to be generated & then stored
            logUserAncestorId.advance()
            currentUserIdsUseCase.updateLogUserId(logUserAncestorId.currentValue)

            logUser(logger, userId, logUserAncestorId.currentValue)
        }
    }

    fun overrideLogUserId(logger: MetricsLogger, logUserId: String) = xray.monitored {
        currentUserIdsUseCase.updateUserId("")
        currentUserIdsUseCase.updateLogUserId(logUserId)
        logUser(logger, "", logUserId)
    }

    private fun logUser(logger: MetricsLogger, userId: String, logUserId: String) {
        // No need to logUser if there are no IDs
        if (userId.isBlank() && logUserId.isBlank()) return

        logger.enqueueMessage(createUserMessage(clock, userId, logUserId))
    }
}
