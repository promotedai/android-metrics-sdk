package ai.promoted

import android.app.Activity
import com.google.protobuf.Message

/**
 * Represents all data associated to an action that is provided by the library user.
 */
data class ActionData(
    val sourceActivity: Activity?,
    val impressionId: String?,
    val insertionId: String?,
    val contentId: String?,
    val requestId: String?,
    val autoViewId: String?,
    val elementId: String?,
    val targetUrl: String?,
    val hasSuperimposedViews: Boolean?,
    val customProperties: Message?
) {
    data class Builder(
        var impressionId: String? = null,
        var insertionId: String? = null,
        var contentId: String? = null,
        var requestId: String? = null,
        var autoViewId: String? = null,
        var elementId: String? = null,
        var targetUrl: String? = null,
        var hasSuperimposedViews: Boolean? = null,
        var customProperties: Message? = null
    ) {
        fun build(sourceActivity: Activity?) = ActionData(
            sourceActivity, impressionId, insertionId, contentId, requestId, autoViewId, elementId, targetUrl,
            hasSuperimposedViews, customProperties
        )
    }
}
