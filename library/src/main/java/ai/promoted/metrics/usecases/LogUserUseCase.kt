package ai.promoted.metrics.usecases

import ai.promoted.internal.Clock
import ai.promoted.metrics.MetricsLogger

internal class LogUserUseCase(
    logger: MetricsLogger,
    private val clock: Clock
) : LoggingUseCase(logger) {
    fun logUser() = logger.enqueueMessage(createUserMessage(clock))
}