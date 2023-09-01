package ai.promoted.metrics.usecases.anomaly

import ai.promoted.proto.event.LogRequest
import ai.promoted.proto.event.Impression
import ai.promoted.proto.event.Action
import ai.promoted.proto.event.User
import ai.promoted.proto.event.ImpressionSourceType
import ai.promoted.proto.event.ActionType
import ai.promoted.xray.Xray
import com.google.protobuf.Message

internal class AnalyzeMessageUseCase(
    private val anomalyHandler: AnomalyHandler,
    private val xray: Xray
) {
    fun analyzeMessage(message: Message) = xray.monitored {
        // No need to analyze if we aren't handling anomalies
        if(anomalyHandler is NoOpAnomalyHandler) return@monitored

        when (message) {
            is LogRequest -> analyzeLogRequest(message)
            is Impression -> analyzeImpression(message)
            is Action -> analyzeAction(message)
            is User -> analyzeUser(message)
            else -> {}
        }
    }

    private fun analyzeLogRequest(logRequest: LogRequest) {
        if (logRequest.userInfo?.anonUserId.isNullOrBlank()) {
            anomalyHandler.handle(AnomalyType.MissingAnonUserIdInLogRequest)
        }
    }

    private fun analyzeImpression(impression: Impression) {
        if (impression.sourceType == ImpressionSourceType.DELIVERY
            && impression.insertionId.isNullOrBlank()
            && impression.contentId.isNullOrBlank()
        ) {
            anomalyHandler.handle(AnomalyType.MissingJoinableFieldsInImpression)
        }
    }

    @Suppress("ComplexCondition")
    private fun analyzeAction(action: Action) {
        if (action.actionType != ActionType.CHECKOUT
            && action.actionType != ActionType.PURCHASE
            && action.impressionId.isNullOrBlank()
            && action.insertionId.isNullOrBlank()
            && action.contentId.isNullOrBlank()
        ) {
            anomalyHandler.handle(AnomalyType.MissingJoinableFieldsInAction)
        }
    }

    private fun analyzeUser(user: User) {
        if (user.userInfo?.anonUserId.isNullOrBlank()) {
            anomalyHandler.handle(AnomalyType.MissingAnonUserIdInUserMessage)
        }
    }
}
