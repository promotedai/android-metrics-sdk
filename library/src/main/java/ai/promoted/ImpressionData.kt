package ai.promoted

import android.app.Activity
import com.google.protobuf.Message

/**
 * Represents all data associated to an impression that is provided by the library user.
 */
data class ImpressionData(
    val sourceActivity: Activity?,
    val insertionId: String?,
    val contentId: String?,
    val requestId: String?,
    val customProperties: Message?
) {
    data class Builder(
        var insertionId: String? = null,
        var contentId: String? = null,
        var requestId: String? = null,
        var customProperties: Message? = null
    ) {
        fun build(sourceActivity: Activity?) = ImpressionData(
            sourceActivity, insertionId, contentId, requestId, customProperties
        )
    }
}

