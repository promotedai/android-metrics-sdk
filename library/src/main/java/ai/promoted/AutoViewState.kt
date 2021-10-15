package ai.promoted

/**
 * This class is typically only for consumption by our React Native library. It allows for
 * manual passage of auto-view related fields that are determined in the RN view layer.
 */
data class AutoViewState(
    val autoViewId: String?,
    val hasSuperimposedViews: Boolean?
)
