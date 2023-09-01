package ai.promoted.metrics.usecases.anomaly

import ai.promoted.mockkRelaxedUnit
import ai.promoted.proto.common.UserInfo
import ai.promoted.proto.event.*
import ai.promoted.xray.NoOpXray
import io.mockk.spyk
import io.mockk.verify
import org.junit.Test

internal class AnalyzeMessageUseCaseTest {
    private val mockAnomalyHandler = mockkRelaxedUnit<AnomalyHandler>()
    private val useCase = AnalyzeMessageUseCase(
        anomalyHandler = mockAnomalyHandler,
        xray = NoOpXray()
    )

    @Test
    fun `No-op anomaly handler is never invoked`() {
        // Given the anomaly handler is a no op
        val noOpAnomalySpy = spyk(NoOpAnomalyHandler())
        val useCaseWithNoOpHandler = AnalyzeMessageUseCase(
            anomalyHandler = noOpAnomalySpy,
            xray = NoOpXray()
        )

        // When an anomaly message is analyzed
        useCaseWithNoOpHandler.analyzeMessage(User.newBuilder().build())

        // Then the anomaly handler was never invoked
        verify(exactly = 0) { noOpAnomalySpy.handle(any()) }
    }

    @Test
    fun `Anomaly handler is invoked for bad LogRequest`() {
        // Given a bad LogRequest
        val badLogRequest = LogRequest.newBuilder().build()

        // When an anomaly message is analyzed
        useCase.analyzeMessage(badLogRequest)

        // Then the anomaly handler was invoked
        verify { mockAnomalyHandler.handle(AnomalyType.MissingAnonUserIdInLogRequest) }
    }

    @Test
    fun `Anomaly handler is not invoked for good LogRequest`() {
        // Given a good LogRequest
        val goodLogRequest = LogRequest.newBuilder().apply {
            userInfo = UserInfo.newBuilder().apply {
                anonUserId = "test"
            }.build()
        }.build()

        // When a good message is analyzed
        useCase.analyzeMessage(goodLogRequest)

        // Then the anomaly handler was NOT invoked
        verify(exactly = 0) { mockAnomalyHandler.handle(any()) }
    }

    @Test
    fun `Anomaly handler is invoked for bad Impression`() {
        // Given a bad Impression
        val badImpression = Impression.newBuilder().apply {
            sourceType = ImpressionSourceType.DELIVERY
        }.build()

        // When an anomaly message is analyzed
        useCase.analyzeMessage(badImpression)

        // Then the anomaly handler was invoked
        verify { mockAnomalyHandler.handle(AnomalyType.MissingJoinableFieldsInImpression) }
    }

    @Test
    fun `Anomaly handler is not invoked for good Impression`() {
        // Given a good Impression
        val goodImpression1 = Impression.newBuilder().apply {
            sourceType = ImpressionSourceType.DELIVERY
            contentId = "test"
        }.build()
        val goodImpression2 = Impression.newBuilder().apply {
            sourceType = ImpressionSourceType.DELIVERY
            contentId = ""
            insertionId = "test"
        }.build()
        val goodImpression3 = Impression.newBuilder().apply {
            sourceType = ImpressionSourceType.CLIENT_BACKEND
        }.build()

        // When an anomaly message is analyzed
        useCase.analyzeMessage(goodImpression1)
        useCase.analyzeMessage(goodImpression2)
        useCase.analyzeMessage(goodImpression3)

        // Then the anomaly handler was invoked
        verify(exactly = 0) { mockAnomalyHandler.handle(any()) }
    }

    @Test
    fun `Anomaly handler is invoked for bad Action`() {
        // Given a bad Action
        val badAction = Action.newBuilder().build()

        // When an anomaly message is analyzed
        useCase.analyzeMessage(badAction)

        // Then the anomaly handler was invoked
        verify { mockAnomalyHandler.handle(AnomalyType.MissingJoinableFieldsInAction) }
    }

    @Test
    fun `Anomaly handler is not invoked for good Action`() {
        // Given a good Action
        val goodAction1 = Action.newBuilder().apply {
            actionType = ActionType.NAVIGATE
            impressionId = "test"
        }.build()
        val goodAction2 = Action.newBuilder().apply {
            insertionId = "test"
        }.build()
        val goodAction3 = Action.newBuilder().apply {
            impressionId = "test"
        }.build()
        val goodAction4 = Action.newBuilder().apply {
            actionType = ActionType.CHECKOUT
        }.build()
        val goodAction5 = Action.newBuilder().apply {
            actionType = ActionType.PURCHASE
        }.build()

        // When an anomaly message is analyzed
        useCase.analyzeMessage(goodAction1)
        useCase.analyzeMessage(goodAction2)
        useCase.analyzeMessage(goodAction3)
        useCase.analyzeMessage(goodAction4)
        useCase.analyzeMessage(goodAction5)

        // Then the anomaly handler was invoked
        verify(exactly = 0) { mockAnomalyHandler.handle(any()) }
    }

    @Test
    fun `Anomaly handler is invoked for bad User`() {
        // Given a bad User
        val badUser = User.newBuilder().build()

        // When an anomaly message is analyzed
        useCase.analyzeMessage(badUser)

        // Then the anomaly handler was invoked
        verify { mockAnomalyHandler.handle(AnomalyType.MissingAnonUserIdInUserMessage) }
    }

    @Test
    fun `Anomaly handler is not invoked for good User`() {
        // Given a good User
        val goodUser = User.newBuilder().apply {
            userInfo = UserInfo.newBuilder().apply {
                anonUserId = "test"
            }.build()
        }.build()

        // When a good message is analyzed
        useCase.analyzeMessage(goodUser)

        // Then the anomaly handler was NOT invoked
        verify(exactly = 0) { mockAnomalyHandler.handle(any()) }
    }
}
