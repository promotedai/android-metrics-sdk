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
    private val anonUserAncestorId = AncestorId(idGenerator)

    val currentOrNullUserId: String?
        get() {
            val currentUserId = currentUserIdsUseCase.currentUserId
            return currentUserId.ifEmpty { null }
        }

    val currentAnonUserId: String
        get() = currentUserIdsUseCase.currentAnonUserId

    val currentOrPendingAnonUserId: String
        get() {
            val currentAnonUserId = this.currentAnonUserId
            return currentAnonUserId.ifEmpty { anonUserAncestorId.currentOrPendingValue }
        }

    val currentOrNullAnonUserId: String?
        get() {
            val currentAnonUserId = this.currentAnonUserId
            return currentAnonUserId.ifEmpty { null }
        }

    fun setUserId(logger: MetricsLogger, userId: String) = xray.monitored {
        // if it's a new user ID (different value than what's in the store), store it in memory
        // & persist it.
        if (currentUserIdsUseCase.currentUserId != userId) {
            currentUserIdsUseCase.updateUserId(userId)

            // Now that a new user ID exists, a new anonUserId needs to be generated & then stored
            anonUserAncestorId.advance()
            currentUserIdsUseCase.updateAnonUserId(anonUserAncestorId.currentValue)

            logUser(logger, userId, anonUserAncestorId.currentValue)
        }
    }

    fun overrideAnonUserId(logger: MetricsLogger, anonUserId: String) = xray.monitored {
        currentUserIdsUseCase.updateUserId("")
        currentUserIdsUseCase.updateAnonUserId(anonUserId)
        logUser(logger, "", anonUserId)
    }

    private fun logUser(logger: MetricsLogger, userId: String, anonUserId: String) {
        // No need to logUser if there are no IDs
        if (userId.isBlank() && anonUserId.isBlank()) return

        logger.enqueueMessage(createUserMessage(clock, userId, anonUserId))
    }
}
