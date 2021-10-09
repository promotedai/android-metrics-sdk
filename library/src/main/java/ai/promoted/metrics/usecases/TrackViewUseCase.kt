package ai.promoted.metrics.usecases

import ai.promoted.metrics.MetricsLogger
import ai.promoted.metrics.id.AdvanceableId
import ai.promoted.metrics.id.AncestorId
import ai.promoted.metrics.id.IdGenerator
import ai.promoted.platform.Clock
import ai.promoted.platform.DeviceInfoProvider
import ai.promoted.xray.Xray

/**
 * Allows you to track when a view has become visible/focused.
 *
 * This class makes use of [AdvanceableId], which means that you can query [autoViewId] prior to calling
 * [onViewVisible] in order to track events associated to the soon-to-be-opened view, and when you
 * eventually do call [onViewVisible], that original [autoViewId] value will be used/associated to the
 * view that opens. All subsequent calls to [onViewVisible] will result in a new [autoViewId]
 * being generated.
 *
 * This class should be retained as a singleton in order to preserve the current [autoViewId] across
 * other use cases.
 */
@Suppress("LongParameterList")
internal class TrackViewUseCase(
    private val logger: MetricsLogger,
    private val clock: Clock,
    private val deviceInfoProvider: DeviceInfoProvider,
    idGenerator: IdGenerator,
    private val sessionUseCase: TrackSessionUseCase,
    private val xray: Xray
) {
    val autoViewId = AncestorId(idGenerator)

    private var currentKey: String = ""

    /**
     * If needed (if this [key] is different than the last visible key), generates a new view ID.
     * Then logs a view message via [MetricsLogger].
     */
    internal fun onImplicitViewVisible(key: String) = xray.monitored {
        // The view was already logged, so skip it
        if (key == currentKey) return@monitored

        autoViewId.advance()
        currentKey = key

        logger.enqueueMessage(
            createAutoViewMessage(
                clock = clock,
                deviceInfoProvider = deviceInfoProvider,
                autoViewId = autoViewId.currentValueOrNull,
                sessionId = sessionUseCase.sessionId.currentValueOrNull,
                name = key
            )
        )
    }

    /**
     * Directly logs a view event using the given ID
     */
    // TODO - auto-view upon logView
    fun logView(viewId: String) = xray.monitored {
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

    /**
     * Directly logs an auto-view event using the given ID
     */
    @SuppressWarnings("UnusedPrivateMember")
    fun logAutoView(
        autoViewId: String,
        routeName: String,
        routeKey: String
    ) = xray.monitored {
        logger.enqueueMessage(
            createAutoViewMessage(
                clock = clock,
                deviceInfoProvider = deviceInfoProvider,
                autoViewId = autoViewId,
                sessionId = sessionUseCase.sessionId.currentValueOrNull,
                name = routeName
            )
        )
    }
}
