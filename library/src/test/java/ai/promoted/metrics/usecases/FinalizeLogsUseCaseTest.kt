package ai.promoted.metrics.usecases

import ai.promoted.ClientConfig
import ai.promoted.mockkRelaxedUnit
import ai.promoted.platform.DeviceInfoProvider
import ai.promoted.proto.common.ClientInfo
import ai.promoted.proto.delivery.Insertion
import ai.promoted.proto.delivery.Request
import ai.promoted.proto.event.Action
import ai.promoted.proto.event.Impression
import ai.promoted.proto.event.LogRequest
import ai.promoted.proto.event.User
import ai.promoted.proto.event.View
import ai.promoted.xray.NoOpXray
import com.google.protobuf.Message
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class FinalizeLogsUseCaseTest {
    private val testApiKey = "testApiKey"
    private val testUserId = "testUser"
    private val testLogUserId = "testLogUser"
    private val mockDeviceInfoProvider: DeviceInfoProvider = mockk {
        every { screenWidth } returns 0
        every { screenHeight } returns 0
        every { screenDensity } returns 0f
        every { brand } returns ""
        every { manufacturer } returns ""
        every { model } returns ""
        every { sdkRelease } returns ""
    }
    private val useCase = FinalizeLogsUseCase(
        config = ClientConfig.Builder().apply {
            metricsLoggingApiKey = testApiKey
        }.build(),
        systemLogger = mockkRelaxedUnit(),
        deviceInfoProvider = mockDeviceInfoProvider,
        trackUserUseCase = mockkRelaxedUnit {
            every { currentOrNullUserId } returns testUserId
            every { currentLogUserId } returns testLogUserId
            every { currentOrNullLogUserId } returns testLogUserId
        },
        xray = NoOpXray()
    )

    @Test
    fun `Should set user info on final logs`() {
        // When logs finalized
        val request = useCase.finalizeLogs(emptyList())

        // Then the serialized data contains the correct user info
        val deserializedData = LogRequest.parseFrom(request.bodyData)
        assertThat(deserializedData.userInfo.userId, equalTo(testUserId))
        assertThat(deserializedData.userInfo.logUserId, equalTo(testLogUserId))
    }

    @Test
    fun `Should set client info on final logs`() {
        // When logs finalized
        val request = useCase.finalizeLogs(emptyList())

        // Then the serialized data contains the correct client info
        val deserializedData = LogRequest.parseFrom(request.bodyData)
        assertThat(deserializedData.clientInfo.clientType, equalTo(ClientInfo.ClientType.PLATFORM_CLIENT))
        assertThat(deserializedData.clientInfo.trafficType, equalTo(ClientInfo.TrafficType.PRODUCTION))
    }

    @Test
    fun `API key is included on headers`() {
        val request = useCase.finalizeLogs(emptyList())
        assertThat(request.headers["x-api-key"], equalTo(testApiKey))
    }

    @Test
    fun `Protobuf content type is included on headers when wire format is binary`() {
        // Given we're configured for binary
        val useCase = FinalizeLogsUseCase(
            config = ClientConfig.Builder().apply {
                metricsLoggingWireFormat = ClientConfig.MetricsLoggingWireFormat.Binary
            }.build(),
            systemLogger = mockkRelaxedUnit(),
            deviceInfoProvider = mockDeviceInfoProvider,
            trackUserUseCase = mockkRelaxedUnit {
                every { currentOrNullUserId } returns testUserId
                every { currentLogUserId } returns testLogUserId
                every { currentOrNullLogUserId } returns testLogUserId
            },
            xray = NoOpXray()
        )

        // When
        val request = useCase.finalizeLogs(emptyList())

        // Then
        assertThat(request.headers["content-type"], equalTo("application/protobuf"))
    }

    @Test
    fun `No content type on headers when wire format is json`() {
        // Given we're configured for JSON
        val useCase = FinalizeLogsUseCase(
            config = ClientConfig.Builder().apply {
                metricsLoggingWireFormat = ClientConfig.MetricsLoggingWireFormat.Json
            }.build(),
            systemLogger = mockkRelaxedUnit(),
            deviceInfoProvider = mockDeviceInfoProvider,
            trackUserUseCase = mockkRelaxedUnit {
                every { currentOrNullUserId } returns testUserId
                every { currentLogUserId } returns testLogUserId
                every { currentOrNullLogUserId } returns testLogUserId
            },
            xray = NoOpXray()
        )

        // When
        val request = useCase.finalizeLogs(emptyList())

        // Then
        assertThat(request.headers["content-type"], nullValue())
    }

    @Test
    fun `All known supported event types should be present on final log request`() {
        // Given we have one event for every supported type
        // and one unknown type
        val messages = listOf<Message>(
            User.newBuilder().build(),
            View.newBuilder().build(),
            Request.newBuilder().build(),
            Insertion.newBuilder().build(),
            Impression.newBuilder().build(),
            Action.newBuilder().build(),

            // Unknown type
            mockkRelaxedUnit()
        )

        // When we finalize
        val request = useCase.finalizeLogs(messages)

        // Then the serialized data contains each of the supported messages.
        val deserializedData = LogRequest.parseFrom(request.bodyData)
        assertThat(deserializedData.userCount, equalTo(1))
        assertThat(deserializedData.viewCount, equalTo(1))
        assertThat(deserializedData.requestCount, equalTo(1))
        assertThat(deserializedData.insertionCount, equalTo(1))
        assertThat(deserializedData.impressionCount, equalTo(1))
        assertThat(deserializedData.actionCount, equalTo(1))
    }
}
