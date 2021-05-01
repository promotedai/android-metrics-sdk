package ai.promoted.metrics.usecases

import ai.promoted.ClientConfig
import ai.promoted.internal.SystemLogger
import ai.promoted.networking.PromotedApiRequest
import ai.promoted.proto.delivery.Insertion
import ai.promoted.proto.delivery.Request
import ai.promoted.proto.event.*
import com.google.protobuf.Message
import com.google.protobuf.util.JsonFormat

private const val HEADER_API_KEY = "x-api-key"
private const val PROTOBUF_CONTENT_TYPE = "application/protobuf"

// TODO - unit tests
internal class FinalizeLogsUseCase(
    config: ClientConfig,
    private val systemLogger: SystemLogger,
    private val idStorageUseCase: IdStorageUseCase
) {
    // TODO - dev version
    private val url = config.metricsLoggingUrl
    private val apiKey = config.metricsLoggingApiKey
    private val wireFormat = config.metricsLoggingWireFormat

    fun finalizeLogs(logMessages: List<Message>): PromotedApiRequest {
        val finalizedMessage = prepareLogs(logMessages)
        val bodyData = if (wireFormat == ClientConfig.MetricsLoggingWireFormat.Binary) {
            finalizedMessage.toByteArray()
        } else {
            JsonFormat.printer().print(finalizedMessage).encodeToByteArray()
        }

        return PromotedApiRequest(
            url = url,
            headers = buildHeaders(),
            bodyData = bodyData
        )
    }

    private fun prepareLogs(logMessages: List<Message>): LogRequest {
        val logRequestBuilder = LogRequest.newBuilder()
        logRequestBuilder.userInfo = createUserInfoMessage(
            userId = idStorageUseCase.currentUserId,
            logUserId = idStorageUseCase.currentLogUserId
        )
        logMessages.forEach { logRequestBuilder.addMessage(it) }
        return logRequestBuilder.build()
    }

    private fun LogRequest.Builder.addMessage(message: Message): LogRequest.Builder =
        when (message) {
            is User -> addUser(message)
            is SessionProfile -> addSessionProfile(message)
            is Session -> addSession(message)
            is View -> addView(message)
            is Request -> addRequest(message)
            is Insertion -> addInsertion(message)
            is Impression -> addImpression(message)
            is Action -> addAction(message)
            else -> {
                systemLogger.v("Unknown event type: ${message::class}!")
                this
            }
        }

    private fun buildHeaders(): Map<String, String> {
        val headers = mutableMapOf(
            HEADER_API_KEY to apiKey
        )

        if (wireFormat == ClientConfig.MetricsLoggingWireFormat.Binary)
            headers["content-type"] = PROTOBUF_CONTENT_TYPE

        return headers
    }
}