package ai.promoted.metrics.usecases.anomaly

@Suppress("MagicNumber")
internal enum class AnomalyType(val errorCode: Int) {
    MissingAnonUserIdInUserMessage(101),
    MissingAnonUserIdInLogRequest(102),
    MissingJoinableFieldsInImpression(103),
    MissingJoinableFieldsInAction(104),
    SdkNotInitialized(105);

    @Suppress("MaxLineLength")
    val debugDescription: String
        get() = when (this) {
            MissingAnonUserIdInUserMessage,
            MissingAnonUserIdInLogRequest -> "An event was logged with no log user ID. This may be due to a recent change in Promoted initialization. Examples include changes to your Application class, or calls to the Promoted SDK method startSessionAndLogUser()."
            MissingJoinableFieldsInImpression -> "An impression was logged with no content ID or insertion ID. This may be due to a recent change to a list/collection view, or the data model that powers this view."
            MissingJoinableFieldsInAction -> "An action was logged with no impression ID, content ID, or insertion ID. This may be due to a recent change to a list/collection view, or the data model that powers this view."
            SdkNotInitialized -> "PromotedAi was not initialized correctly. This may be due to a recent change to your Application class. Make sure that PromotedMetricsModule is included in -extraModulesForBridge: (if using React Native), and that your native Application class is invoking PromotedAi.initialize()."
        }
}
