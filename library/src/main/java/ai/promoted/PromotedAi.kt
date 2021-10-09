package ai.promoted

import ai.promoted.proto.event.ActionType
import ai.promoted.sdk.PromotedAiSdk
import ai.promoted.sdk.SdkManager
import android.app.Activity
import android.app.Application
import androidx.recyclerview.widget.RecyclerView

/**
 * Single entry-point for the Promoted.Ai SDK. All SDK operations can be safely executed through
 * here without worrying about underlying dependencies / classes / object management.
 */
@Suppress("TooManyFunctions")
object PromotedAi {
    private val manager = SdkManager()
    private val sdk get() = manager.sdkInstance

    /**
     * @see [PromotedAiSdk.logUserId]
     */
    @JvmStatic
    var logUserId: String
        get() = sdk.logUserId
        set(value) {
            sdk.logUserId = value
        }

    /**
     * @see [PromotedAiSdk.sessionId]
     */
    @JvmStatic
    var sessionId: String
        get() = sdk.sessionId
        set(value) {
            sdk.sessionId = value
        }

    /**
     * @see [PromotedAiSdk.autoViewId]
     */
    @JvmStatic
    @Deprecated("Auto-view ID will be used")
    var viewId: String = ""

    /**
     * @see [SdkManager.initialize]
     */
    @JvmStatic
    fun initialize(application: Application, block: ClientConfig.Builder.() -> Unit) =
        manager.initialize(application, block)

    /**
     * @see [SdkManager.initialize]
     */
    @JvmStatic
    fun initialize(application: Application, config: ClientConfig) =
        manager.initialize(application, config)

    /**
     * @see [SdkManager.configure]
     */
    @JvmStatic
    fun configure(application: Application, block: ClientConfig.Builder.() -> Unit) =
        manager.configure(application, block)

    /**
     * @see [SdkManager.configure]
     */
    @JvmStatic
    fun configure(application: Application, config: ClientConfig) =
        manager.configure(application, config)

    /**
     * @see [SdkManager.shutdown]
     */
    @JvmStatic
    fun shutdown() = manager.shutdown()

    /**
     * @see [PromotedAiSdk.startSession]
     */
    @JvmStatic
    fun startSession(userId: String) = sdk.startSession(userId)

    /**
     * @see [PromotedAiSdk.logView]
     */
    @JvmStatic
    fun logView(viewId: String) = sdk.logView(viewId)

    /**
     * @see [PromotedAiSdk.logAutoView]
     */
    @JvmStatic
    fun logAutoView(
        autoViewId: String,
        routeName: String,
        routeKey: String
    ) = sdk.logAutoView(autoViewId, routeName, routeKey)

    /**
     * @see [PromotedAiSdk.onImpression]
     */
    @JvmStatic
    fun onImpression(
        sourceActivity: Activity?,
        dataBlock: ImpressionData.Builder.() -> Unit
    ) = sdk.onImpression(sourceActivity, dataBlock)

    /**
     * @see [PromotedAiSdk.onImpression]
     */
    @JvmStatic
    fun onImpression(
        data: ImpressionData
    ) = sdk.onImpression(data)

    /**
     * @see [PromotedAiSdk.onAction]
     */
    @JvmStatic
    fun onAction(
        sourceActivity: Activity?,
        name: String,
        type: ActionType,
        dataBlock: (ActionData.Builder.() -> Unit)? = null
    ) = sdk.onAction(sourceActivity, name, type, dataBlock)

    /**
     * @see [PromotedAiSdk.onAction]
     */
    @JvmStatic
    fun onAction(name: String, type: ActionType, data: ActionData) =
        sdk.onAction(name, type, data)

    /**
     * @see [PromotedAiSdk.onCollectionVisible]
     */
    @JvmStatic
    fun onCollectionVisible(
        sourceActivity: Activity?,
        collectionViewKey: String,
        content: List<AbstractContent>
    ) = sdk.onCollectionVisible(sourceActivity, collectionViewKey, content)

    /**
     * @see [PromotedAiSdk.onCollectionUpdated]
     */
    @JvmStatic
    fun onCollectionUpdated(
        sourceActivity: Activity?,
        collectionViewKey: String,
        content: List<AbstractContent>
    ) = sdk.onCollectionUpdated(sourceActivity, collectionViewKey, content)

    /**
     * @see [PromotedAiSdk.onCollectionHidden]
     */
    @JvmStatic
    fun onCollectionHidden(sourceActivity: Activity?, collectionViewKey: String) =
        sdk.onCollectionHidden(sourceActivity, collectionViewKey)

    /**
     * @see [PromotedAiSdk.trackRecyclerView]
     */
    @JvmStatic
    fun trackRecyclerView(
        recyclerView: RecyclerView,
        currentDataProvider: () -> List<AbstractContent>,
        impressionThresholdBlock: (ImpressionThreshold.Builder.() -> Unit)? = null
    ) = sdk.trackRecyclerView(recyclerView, currentDataProvider, impressionThresholdBlock)

    /**
     * @see [PromotedAiSdk.trackRecyclerView]
     */
    @JvmStatic
    fun trackRecyclerView(
        recyclerView: RecyclerView,
        currentDataProvider: () -> List<AbstractContent>,
        impressionThreshold: ImpressionThreshold
    ) = sdk.trackRecyclerView(recyclerView, currentDataProvider, impressionThreshold)

    //region JAVA INTER-OP
    /* Java-idiomatic initialization of the SDK */
    /**
     * Begin building an SDK configuration in a chained fashion, finally resulting in an
     * initialize() or configure() call.
     *
     * Example usage from an [Application] class:
     *
     *     PromotedAi
     *         .buildConfiguration()
     *         .withMetricsLoggingUrl("https://myurl.com")
     *         .withMetricsLoggingApiKey("api-123")
     *         .initialize(this)
     *
     * @see [initialize]
     * @see [configure]
     */
    @JvmStatic
    fun buildConfiguration() = SdkBuilder(manager)

    /**
     * Start a session without a user ID (i.e. a signed out user has begun a session).
     * @see [PromotedAiSdk.startSession]
     */
    /* Default parameter / signed out user */
    @JvmStatic
    fun startSession() = sdk.startSession()

    /* Java-idiomatic building of impression/impression data*/
    /**
     * Begin building an impression in a chained fashion, finally resulting in a log() call.
     *
     * Example usage:
     *
     *     PromotedAi
     *         .buildImpression()
     *         .withInsertionId("insertion-id")
     *         .withCustomProperties(MyCustomProperties())
     *         .log()
     *
     * @see [PromotedAiSdk.onImpression]
     */
    @JvmStatic
    fun buildImpression() = ImpressionBuilder(sdk)

    /* Java-idiomatic building of action/action data*/
    /**
     * Begin building an action in a chained fashion, finally resulting in a log() call.
     *
     * Example usage:
     *
     *     PromotedAi
     *         .buildAction()
     *         .withName("my action")
     *         .withType(ActionType.CUSTOM_ACTION_TYPE)
     *         .withCustomProperties(MyCustomProperties())
     *         .log()
     *
     * @see [PromotedAiSdk.onAction]
     */
    @JvmStatic
    fun buildAction() = ActionBuilder(sdk)

    /* Java-idiomatic way of tracking a RecyclerView */
    /**
     * Begin building a [RecyclerViewTrackingBuilder] configuration in a chained fashion,
     * finally resulting in a startTracking() call.
     *
     * Example usage:
     *
     *     PromotedAi
     *         .buildRecyclerViewTracking()
     *         .withTimeThreshold(1000L)
     *         .startTracking(recyclerView, recyclerViewContentProvider)
     */
    @JvmStatic
    fun buildRecyclerViewTracking() = RecyclerViewTrackingBuilder(sdk)
    //endregion
}
