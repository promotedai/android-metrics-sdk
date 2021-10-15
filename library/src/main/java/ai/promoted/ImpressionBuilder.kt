package ai.promoted

import ai.promoted.sdk.PromotedAiSdk
import android.app.Activity
import com.google.protobuf.Message

/**
 * This class is primarily to provide an easy-to-use Java API, via [PromotedAi.buildImpression],
 * so that users of the library can dynamically set whichever action components they want to set.
 *
 * While this is supported for Kotlin users, the [PromotedAiSdk.onImpression] with the
 * [ImpressionData.Builder] configuration block is recommended.
 */
class ImpressionBuilder internal constructor(private val sdk: PromotedAiSdk) {
    private val dataBuilder = ImpressionData.Builder()

    fun withInsertionId(id: String) = apply { dataBuilder.insertionId = id }
    fun withContentId(id: String) = apply { dataBuilder.contentId = id }
    fun withRequestId(id: String) = apply { dataBuilder.requestId = id }
    fun withAutoViewId(id: String) = apply { dataBuilder.autoViewId = id }
    fun withHasSuperimposedViews(hasSuperimposedViews: Boolean) =
        apply { dataBuilder.hasSuperimposedViews = hasSuperimposedViews }

    fun withCustomProperties(properties: Message) =
        apply { dataBuilder.customProperties = properties }

    fun log(sourceActivity: Activity?) = sdk.onImpression(dataBuilder.build(sourceActivity))
}
