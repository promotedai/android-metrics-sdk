package ai.promoted.metrics.usecases

import ai.promoted.ClientConfig
import ai.promoted.PromotedApiRequest
import ai.promoted.platform.DeviceInfoProvider
import ai.promoted.platform.SystemLogger
import ai.promoted.proto.common.Device
import ai.promoted.proto.delivery.Insertion
import ai.promoted.proto.delivery.Request
import ai.promoted.proto.event.Action
import ai.promoted.proto.event.Impression
import ai.promoted.proto.event.LogRequest
import ai.promoted.proto.event.User
import ai.promoted.proto.event.View
import ai.promoted.xray.Xray
import com.google.protobuf.Message
import com.google.protobuf.util.JsonFormat

private const val HEADER_API_KEY = "x-api-key"
private const val PROTOBUF_CONTENT_TYPE = "application/protobuf"

/**
 * Responsible for taking a list of log messages, preparing them to be sent to the API, and finally
 * returning those prepared log messages in the form of an abstract object that any networking
 * stack can use (the [PromotedApiRequest]).
 *
 * This class can be constructed as needed and does not need to be a singleton; it is purely
 * functional.
 */
internal class FinalizeLogsUseCase(
    config: ClientConfig,
    private val systemLogger: SystemLogger,
    private val deviceInfoProvider: DeviceInfoProvider,
    private val trackUserUseCase: TrackUserUseCase,
    private val xray: Xray
) {
    private val url = config.metricsLoggingUrl
    private val apiKey = config.metricsLoggingApiKey
    private val wireFormat = config.metricsLoggingWireFormat

    // We only need to generate the device message once; the data won't change since it's
    // hardware/device-level info
    private val deviceMessage: Device by lazy {
        createDeviceMessage(deviceInfoProvider)
    }

    /**
     * Transform the list of [Message]s into a [PromotedApiRequest] with the proper API request
     * info. This will inspect the [ClientConfig.metricsLoggingWireFormat] to ensure the ByteArray
     * representing the POST body data is congruent with the library user's wire-format setting.
     */
    fun finalizeLogs(logMessages: List<Message>): PromotedApiRequest = xray.monitored {
        val finalizedMessage = prepareLogs(logMessages)
        val bodyData = if (wireFormat == ClientConfig.MetricsLoggingWireFormat.Binary) {
            finalizedMessage.toByteArray()
        } else {
            JsonFormat.printer().print(finalizedMessage).encodeToByteArray()
        }

        return@monitored PromotedApiRequest(
            url = url,
            headers = buildHeaders(),
            bodyData = bodyData
        )
    }

    private fun prepareLogs(logMessages: List<Message>): LogRequest {
        val logRequestBuilder = LogRequest.newBuilder()
        logRequestBuilder.clientInfo = createClientInfo()
        logRequestBuilder.userInfo = createUserInfoMessage(
            userId = trackUserUseCase.currentOrNullUserId,
            logUserId = trackUserUseCase.currentOrNullLogUserId
        )
        logRequestBuilder.device = deviceMessage
        logMessages.forEach { logRequestBuilder.addMessage(it) }
        return logRequestBuilder.build()
    }

    private fun LogRequest.Builder.addMessage(message: Message): LogRequest.Builder =
        when (message) {
            is User -> addUser(message)
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
