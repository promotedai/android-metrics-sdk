package ai.promoted.metrics.usecases

import ai.promoted.ClientConfig
import ai.promoted.networking.PromotedApiRequest
import com.google.protobuf.Message

internal class FinalizeLogsUseCase(config: ClientConfig) {
    // TODO - dev version
    private val url = config.metricsLoggingUrl
    private val apiKey = config.metricsLoggingApiKey
    private val wireFormat = config.metricsLoggingWireFormat

    fun finalizeLogs(logMessages: List<Message>): PromotedApiRequest {
        return PromotedApiRequest(
            url = url,
            headers = mapOf(),
            bodyData = ByteArray(0)
        )
    }
}