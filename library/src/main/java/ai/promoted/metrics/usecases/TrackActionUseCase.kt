package ai.promoted.metrics.usecases

import ai.promoted.internal.Clock
import ai.promoted.metrics.ActionData
import ai.promoted.metrics.InternalActionData
import ai.promoted.metrics.MetricsLogger
import ai.promoted.metrics.id.IdGenerator
import ai.promoted.proto.event.ActionType

/**
 * Allows you to track a user's action and all associated metadata.
 *
 * This class can be constructed as needed and does not need to be a singleton; it is purely
 * functional. It does depend on a global viewId and sessionId, but those are provided by
 * the session & view use cases that should themselves be singletons.
 */
internal class TrackActionUseCase(
    private val clock: Clock,
    private val logger: MetricsLogger,
    private val idGenerator: IdGenerator,
    private val impressionIdGenerator: ImpressionIdGenerator,
    private val sessionUseCase: TrackSessionUseCase,
    private val viewUseCase: TrackViewUseCase
) {
    /**
     * Logs the given action, along with any additional data associated to it (as provided by the
     * [dataBlock]). An example call would look like:
     *
     * onAction("name", ActionType.TYPE) {
     *     insertionId = "id"
     *     customProprties = SomeProps()
     * }
     *
     * All that [dataBlock] is a lambda which receives an [ActionData.Builder] as its context,
     * so that you can granularly choose which additional data you want to be tied to this action.
     */
    fun onAction(
        name: String,
        type: ActionType,
        dataBlock: (ActionData.Builder.() -> Unit)?
    ) {
        val data = if (dataBlock != null) ActionData.Builder().apply(dataBlock).build()
        else ActionData.Builder().build()

        onAction(name, type, data)
    }

    private fun onAction(name: String, type: ActionType, data: ActionData) {
        val actionId = idGenerator.newId()
        val impressionId =
            impressionIdGenerator.generateImpressionId(data.insertionId, data.contentId)

        val internalActionData = InternalActionData(
            name = name,
            type = type,
            actionId = actionId,
            impressionId = impressionId,
            sessionId = sessionUseCase.sessionId,
            viewId = viewUseCase.viewId
        )

        logger.enqueueMessage(
            createActionMessage(
                clock = clock,
                internalActionData = internalActionData,
                actionData = data
            )
        )
    }
}
