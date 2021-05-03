package ai.promoted.metrics.usecases

import ai.promoted.internal.Clock
import ai.promoted.metrics.MetricsLogger
import ai.promoted.metrics.id.IdGenerator
import ai.promoted.proto.event.ActionType

/**
 * Allows you to track a user's action and all associated metadata.
 */
internal class TrackActionUseCase(
    private val clock: Clock,
    private val logger: MetricsLogger,
    private val idGenerator: IdGenerator,
    private val viewUseCase: TrackViewUseCase,
    private val sessionUseCase: TrackSessionUseCase
) {
    fun onAction(
        name: String,
        type: ActionType,
        contentId: String?,
        insertionId: String?,
        requestId: String?,
        targetUrl: String?,
        elementId: String?
    ) {
        // TODO
        val actionId = idGenerator.newId()
        val impressionId = ""

        logger.enqueueMessage(
            createActionMessage(
                clock = clock,
                name = name,
                actionId = actionId,
                sessionId = sessionUseCase.sessionId,
                viewId = viewUseCase.viewId,
                type = type,
                impressionId = impressionId,
                insertionId = insertionId,
                requestId = requestId,
                elementId = elementId,
                targetUrl = targetUrl
            )
        )
    }
}
