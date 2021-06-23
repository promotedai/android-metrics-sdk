package ai.promoted.metrics.usecases

import ai.promoted.metrics.MetricsLogger
import ai.promoted.metrics.id.AdvanceableId
import ai.promoted.metrics.id.AncestorId
import ai.promoted.metrics.id.IdGenerator
import ai.promoted.platform.Clock
import ai.promoted.platform.SystemLogger
import ai.promoted.xray.Xray

/**
 * Allows you to track the start of an app session.
 *
 * This class makes use of [AdvanceableId], which means that you can query [sessionId] prior to
 * calling [startSession] in order to track events associated to the soon-to-be-started session, and
 * when you eventually do call [startSession], that original [sessionId] value will be
 * used/associated to the session that starts. All subsequent calls to [startSession] will result in
 * a new [sessionId] being generated.
 *
 * This class should be retained as a singleton in order to preserve the current [sessionId] across
 * other use cases.
 */
internal class TrackSessionUseCase(
    private val systemLogger: SystemLogger,
    private val clock: Clock,
    private val logger: MetricsLogger,
    idGenerator: IdGenerator,
    private val trackUserUseCase: TrackUserUseCase,
    private val xray: Xray
) {
    val sessionId = AncestorId(idGenerator)

    /**
     * If needed, generates a new session ID, then ensures that the logUserId is in sync with the
     * user ID, and then logs both a user message and a session message using [MetricsLogger].
     */
    fun startSession(userId: String) = xray.monitored {
        if (sessionId.isOverridden) {
            systemLogger.e(
                IllegalStateException(
                    "Attempted to start a new session after " +
                            "overriding session ID"
                )
            )
            return@monitored
        }

        trackUserUseCase.setUserId(userId)

        sessionId.advance()
        logSession()
    }


    private fun logSession() = logger.enqueueMessage(createSessionMessage(clock))
}
