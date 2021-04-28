package ai.promoted.metrics

import com.google.protobuf.Message

interface MetricsLogger {
    fun startSessionAndLogUser(userId: String)
    fun startSessionAndLogSignedOutUser()

    fun logUser(properties: Message? = null)

    fun shutdown()
}