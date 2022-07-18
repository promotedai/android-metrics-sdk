package ai.promoted.metrics.usecases.anomaly

internal enum class AnomalyType(val errorCode: Int) {
    MissingLogUserIdInUserMessage(101),
    MissingLogUserIdInLogRequest(102),
    MissingJoinableFieldsInImpression(103),
    MissingJoinableFieldsInAction(104),
    ReactNativeMetricsModuleNotInitialized(105);

    val debugDescription: String
        get() = when (this) {
            MissingLogUserIdInUserMessage,
            MissingLogUserIdInLogRequest -> "An event was logged with no log user ID. This may be due to a recent change in Promoted initialization. Examples include changes to your AppDelegate class, or calls to the Promoted SDK method startSessionAndLogUser()."
            MissingJoinableFieldsInImpression -> "An impression was logged with no content ID or insertion ID. This may be due to a recent change to a list/collection view, or the data model that powers this view."
            MissingJoinableFieldsInAction -> "An action was logged with no impression ID, content ID, or insertion ID. This may be due to a recent change to a list/collection view, or the data model that powers this view."
            ReactNativeMetricsModuleNotInitialized -> "PromotedMetricsModule was not initialized correctly. This may be due to a recent change to AppDelegate. Make sure that PromotedMetricsModule is included in -extraModulesForBridge:."
        }
}