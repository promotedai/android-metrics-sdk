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

    private var currentKey: String = ""

    private val deviceMessage: Device by lazy {
        createDeviceMessage(deviceInfoProvider)
    }

    /**
     * If needed (if this [key] is different than the last visible key), generates a new view ID.
     * Then logs a view message via [MetricsLogger].
     */
    fun onViewVisible(key: String) = xray.monitored {
        if(viewId.isOverridden) {
            systemLogger.e(IllegalStateException("Attempted to start a new session after " +
                    "overriding session ID"))
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
}
