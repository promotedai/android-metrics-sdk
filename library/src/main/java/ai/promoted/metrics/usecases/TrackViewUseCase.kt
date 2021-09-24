package ai.promoted.metrics.usecases

import ai.promoted.metrics.MetricsLogger
import ai.promoted.metrics.id.AdvanceableId
import ai.promoted.metrics.id.AncestorId
import ai.promoted.metrics.id.IdGenerator
import ai.promoted.platform.Clock
import ai.promoted.platform.DeviceInfoProvider
import ai.promoted.platform.SystemLogger
import ai.promoted.proto.event.Device
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
    val viewId = AncestorId(idGenerator)

    /**
     * Allows the clients to set an externally-generated view ID to be used on the next view event.
     * Once the next view is logged, this value is cleared.
     */
    var externalViewId: String? = null

    private var currentKey: String = ""

    private val deviceMessage: Device by lazy {
        createDeviceMessage(deviceInfoProvider)
    }

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
                viewId = viewId.currentValueOrNull,
                sessionId = sessionUseCase.sessionId.currentValueOrNull,
                name = key,
                deviceMessage = deviceMessage
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
            createImplicitViewMessage(
                clock = clock,
                viewId = viewId.currentValueOrNull,
                externalViewId = externalViewId,
                sessionId = sessionUseCase.sessionId.currentValueOrNull,
                name = key,
                deviceMessage = deviceMessage
            )
        )

        externalViewId = null
    }

    fun logView(viewId: String) = xray.monitored {
        // We don't actually need this overridden per se, but what it will do is prevent any
        // further usage of onViewVisible, so as to avoid conflicts of view ID strategy
        this.viewId.override(viewId)
        logger.enqueueMessage(
            createViewMessage(
                clock = clock,
                viewId = viewId,
                sessionId = sessionUseCase.sessionId.currentValueOrNull,
                name = viewId,
                deviceMessage = deviceMessage
            )
        )
    }
}
