package ai.promoted.metrics.usecases

import ai.promoted.metrics.MetricsLogger
import ai.promoted.metrics.id.AdvanceableId
import ai.promoted.metrics.id.AncestorId
import ai.promoted.metrics.id.IdGenerator
import ai.promoted.platform.Clock
import ai.promoted.platform.DeviceInfoProvider
import ai.promoted.platform.SystemLogger
import ai.promoted.xray.Xray

/**
 * Allows you to track when a view has become visible/focused.
 *
 * This class makes use of [AdvanceableId], which means that you can query [viewId] prior to calling
 * [onViewVisible] in order to track events associated to the soon-to-be-opened view, and when you
 * eventually do call [onViewVisible], that original [viewId] value will be used/associated to the
 * view that opens. All subsequent calls to [onViewVisible] will result in a new [viewId]
 * being generated.
 *
 * This class should be retained as a singleton in order to preserve the current [viewId] across
 * other use cases.
 */
@Suppress("LongParameterList")
internal class TrackViewUseCase(
    private val systemLogger: SystemLogger,
    private val logger: MetricsLogger,
    private val clock: Clock,
    private val deviceInfoProvider: DeviceInfoProvider,
    idGenerator: IdGenerator,
    private val sessionUseCase: TrackSessionUseCase,
    private val xray: Xray
) {
    // TODO - refactor to autoViewId
    val viewId = AncestorId(idGenerator)

    /**
     * Allows the clients to set an externally-generated view ID to be used on the next view event.
     * Once the next view is logged, this value is cleared.
     */
    // TODO - revisit usage
    var externalViewId: String? = null

    private var currentKey: String = ""

    /**
     * If needed (if this [key] is different than the last visible key), generates a new view ID.
     * Then logs a view message via [MetricsLogger].
     */
    fun onViewVisible(key: String) = xray.monitored {
        if (viewId.isOverridden) {
            systemLogger.e(
                IllegalStateException(
                    "Attempted to log a new view after " +
                            "overriding view ID"
                )
            )
            return@monitored
        }

        if (key != currentKey) {
            viewId.advance()
            currentKey = key
        }

        logger.enqueueMessage(
            createViewMessage(
                clock = clock,
                deviceInfoProvider = deviceInfoProvider,
                viewId = viewId.currentValueOrNull,
                sessionId = sessionUseCase.sessionId.currentValueOrNull,
                name = key
            )
        )
    }

    /**
     * If needed (if this [key] is different than the last visible key), generates a new view ID.
     * Then logs a view message via [MetricsLogger].
     */
    internal fun onImplicitViewVisible(key: String) = xray.monitored {
        if (viewId.isOverridden) {
            systemLogger.e(
                IllegalStateException(
                    "Attempted to log a new view after " +
                            "overriding view ID"
                )
            )
            return@monitored
        }

        // The view was already logged, so skip it
        if (key == currentKey) return@monitored

        viewId.advance()
        currentKey = key

        // TODO - auto-view schema + external view ID
        logger.enqueueMessage(
            createAutoViewMessage(
                clock = clock,
                deviceInfoProvider = deviceInfoProvider,
                autoViewId = viewId.currentValueOrNull,
                sessionId = sessionUseCase.sessionId.currentValueOrNull,
                name = key
            )
        )

        externalViewId = null
    }

    fun logView(viewId: String) = xray.monitored {
        // We don't actually need this overridden per se, but what it will do is prevent any
        // further usage of onViewVisible, so as to avoid conflicts of view ID strategy
        // TODO - revisit w/ auto-view refactor - remove override
        this.viewId.override(viewId)
        logger.enqueueMessage(
            createViewMessage(
                clock = clock,
                deviceInfoProvider = deviceInfoProvider,
                viewId = viewId,
                sessionId = sessionUseCase.sessionId.currentValueOrNull,
                name = viewId
            )
        )
    }

    // TODO
//    fun logAutoView(autoViewId: String) = xray.monitored {
//
//    }
}
