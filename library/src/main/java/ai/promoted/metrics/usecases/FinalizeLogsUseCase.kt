package ai.promoted.metrics.usecases

import ai.promoted.ClientConfig
import ai.promoted.networking.PromotedApiRequest
import com.google.protobuf.Message

private const val HEADER_API_KEY = "x-api-key"
private const val PROTOBUF_CONTENT_TYPE = "application/protobuf"

internal class FinalizeLogsUseCase(config: ClientConfig) {
    // TODO - dev version
    private val url = config.metricsLoggingUrl
    private val apiKey = config.metricsLoggingApiKey
    private val wireFormat = config.metricsLoggingWireFormat

    fun finalizeLogs(logMessages: List<Message>): PromotedApiRequest {
        return PromotedApiRequest(
            url = url,
            headers = buildHeaders(),
            bodyData = ByteArray(0)
        )
    }

    private fun buildHeaders(): Map<String, String> {
        val headers = mutableMapOf(
            HEADER_API_KEY to apiKey
        )

        if(wireFormat == ClientConfig.MetricsLoggingWireFormat.Binary)
            headers["content-type"] = PROTOBUF_CONTENT_TYPE

        return headers
    }
}