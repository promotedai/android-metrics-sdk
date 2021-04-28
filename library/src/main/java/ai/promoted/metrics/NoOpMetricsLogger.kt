package ai.promoted.metrics

import com.google.protobuf.Message

internal abstract class NoOpMetricsLogger : MetricsLogger {
    override fun shutdown() {}
    override fun startSessionAndLogUser(userId: String) {}
    override fun startSessionAndLogSignedOutUser() {}
    override fun logUser(properties: Message?) {}

    companion object : NoOpMetricsLogger()
}