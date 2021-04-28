package ai.promoted.metrics

import ai.promoted.ClientConfig
import com.google.protobuf.Message
import java.util.concurrent.TimeUnit

internal class DefaultMetricsLogger constructor(
    config: ClientConfig
) : MetricsLogger {
    private val scheduler = OperationScheduler(
        intervalMillis = TimeUnit.SECONDS.toMillis(config.loggingFlushInterval),
        operation = this::flush
    )

    private var logMessages = mutableListOf<Message>()

    override fun startSessionAndLogUser(userId: String) {
        TODO("Not yet implemented")
    }

    override fun startSessionAndLogSignedOutUser() {
        TODO("Not yet implemented")
    }

    override fun logUser(properties: Message?) {
        TODO("Not yet implemented")
    }

    override fun shutdown() {
        scheduler.cancel()
    }

    private fun flush() {
        // TODO
    }
}