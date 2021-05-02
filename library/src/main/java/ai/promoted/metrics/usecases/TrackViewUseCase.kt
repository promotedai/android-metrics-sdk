package ai.promoted.metrics.usecases

import ai.promoted.internal.Clock
import ai.promoted.internal.DeviceInfoProvider
import ai.promoted.metrics.MetricsLogger
import ai.promoted.metrics.id.AdvanceableId
import ai.promoted.metrics.id.IdGenerator
import ai.promoted.proto.event.Device

internal class TrackViewUseCase(
    private val logger: MetricsLogger,
    private val clock: Clock,
    private val deviceInfoProvider: DeviceInfoProvider,
    idGenerator: IdGenerator,
    private val sessionUseCase: TrackSessionUseCase
) {
    private val advanceableViewId = AdvanceableId(
        skipFirstAdvancement = true,
        idGenerator = idGenerator
    )

    val viewId: String
        get() = advanceableViewId.currentValue

    private var currentKey: String = ""

    private val deviceMessage: Device by lazy {
        createDeviceMessage(deviceInfoProvider)
    }

    fun onViewVisible(key: String) {
        if (key != currentKey) {
            advanceableViewId.advance()
            currentKey = key
        }

        logger.enqueueMessage(
            createViewMessage(
                clock = clock,
                viewId = viewId,
                sessionId = sessionUseCase.sessionId,
                name = key,
                deviceMessage = deviceMessage
            )
        )
    }
}
