package ai.promoted.metrics.usecases

import ai.promoted.internal.Clock
import ai.promoted.metrics.ActionData
import ai.promoted.metrics.InternalActionData
import ai.promoted.metrics.MetricsLogger
import ai.promoted.metrics.id.IdGenerator

/**
 * Allows you to track a user's action and all associated metadata.
 */
internal class TrackActionUseCase(
    private val clock: Clock,
    private val logger: MetricsLogger,
    private val idGenerator: IdGenerator,
    private val currentUserIdsUseCase: CurrentUserIdsUseCase,
    private val sessionUseCase: TrackSessionUseCase,
    private val viewUseCase: TrackViewUseCase
) {
    fun onAction(actionData: ActionData) {
        val actionId = idGenerator.newId()
        val impressionId = generateImpressionId(actionData)

        val internalActionData = InternalActionData(
            actionId = actionId,
            impressionId = impressionId,
            sessionId = sessionUseCase.sessionId,
            viewId = viewUseCase.viewId
        )

        logger.enqueueMessage(
            createActionMessage(
                clock = clock,
                internalActionData = internalActionData,
                actionData = actionData
            )
        )
    }

    private fun generateImpressionId(metadata: ActionData): String? = when {
        metadata.insertionId != null -> idGenerator.newId(basedOn = metadata.insertionId)
        metadata.contentId != null -> idGenerator.newId(
            basedOn = (metadata.contentId + currentUserIdsUseCase.currentLogUserId)
        )
        else -> null
    }
}
