package ai.promoted.metrics.usecases

import ai.promoted.ActionData
import ai.promoted.metrics.InternalActionData
import ai.promoted.metrics.MetricsLogger
import ai.promoted.metrics.id.IdGenerator
import ai.promoted.platform.Clock
import ai.promoted.proto.event.ActionType
import ai.promoted.xray.Xray
import android.app.Activity

/**
 * Allows you to track a user's action and all associated metadata.
 *
 * This class can be constructed as needed and does not need to be a singleton; it is purely
 * functional. It does depend on a global viewId and sessionId, but those are provided by
 * the session & view use cases that should themselves be singletons.
 */
@Suppress("LongParameterList")
internal class TrackActionUseCase(
    private val clock: Clock,
    private val logger: MetricsLogger,
    private val idGenerator: IdGenerator,
    private val sessionUseCase: TrackSessionUseCase,
    private val viewUseCase: TrackViewUseCase,
    private val xray: Xray
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
     * The [dataBlock] simply is a lambda which receives an [ActionData.Builder] as its context,
     * so that you can granularly choose which additional data you want to be tied to this action.
     */
    fun onAction(
        sourceActivity: Activity?,
        name: String,
        type: ActionType,
        dataBlock: (ActionData.Builder.() -> Unit)?
    ) {
        val data = if (dataBlock != null) {
            ActionData.Builder().apply(dataBlock).build(sourceActivity)
        } else ActionData.Builder().build(sourceActivity)

        onAction(name, type, data)
    }

    /**
     * Logs the given action, along with any additional data associated to it.
     */
    fun onAction(name: String, type: ActionType, data: ActionData) = xray.monitored {
        val actionId = idGenerator.newId()

        // Log a new view event if necessary
        data.sourceActivity?.let { viewUseCase.onImplicitViewVisible(it::class.java.name) }

        // Allows for manual passing in of hasSuperImposedViews (i.e. via RN) or an inferred
        // value if one exists
        val hasSuperImposedViews = when (data.hasSuperImposedViews) {
            true -> true
            false -> false
            else -> data.sourceActivity?.hasWindowFocus() == false
        }

        val internalActionData = InternalActionData(
            name = name,
            type = type,
            actionId = actionId,
            sessionId = sessionUseCase.sessionId.currentValueOrNull,
            autoViewId = viewUseCase.autoViewId.currentValueOrNull,
            hasSuperImposedViews = hasSuperImposedViews
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
