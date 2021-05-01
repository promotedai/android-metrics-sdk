package ai.promoted.metrics.usecases

import ai.promoted.internal.Clock
import ai.promoted.metrics.MetricsLogger

internal class LogSessionUseCase(
    logger: MetricsLogger,
    private val clock: Clock
) : LoggingUseCase(logger) {
    fun logSession() = logger.enqueueMessage(createSessionMessage(clock))
}