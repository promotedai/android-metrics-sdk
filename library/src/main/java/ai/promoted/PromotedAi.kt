package ai.promoted

import ai.promoted.metrics.AbstractContent
import ai.promoted.metrics.ActionData
import ai.promoted.proto.event.ActionType

/**
 * The public-facing API for interacting with Promoted.Ai. Instances are managed internally by
 * the SDK.
 */
interface PromotedAi {
    fun startSession(userId: String = "")
    fun onViewVisible(key: String)

    fun onAction(
        name: String,
        type: ActionType,
        dataBlock: (ActionData.Builder.() -> Unit)? = null
    )

    fun onAction(
        name: String,
        type: ActionType,
        data: ActionData
    )

    fun onCollectionVisible(collectionViewKey: String, content: List<AbstractContent>)
    fun onCollectionHidden(collectionViewKey: String)

    fun shutdown()

    /*
        Note: This object extends PromotedAiManager while also implementing PromotedAi. This is to
        allow for a simple API for the library users, so that they can jointly execute management
        functions (i.e. initialize(), configure(), shutdown()) along with PromotedAi logging
        functions (i.e. startSession()) all from one single interface (this companion object, a.k.a.
        referenced in code as PromotedAi.initialize(), PromotedAi.startSession(), etc.).

        Because this object extends the manager and also implements the main interface, that means
        that the shutdown() function of PromotedAiManager(), which is final, prevents this
        object overriding the PromotedAi.shutdown() interface function. While it might be cause for
        confusion upon first inspection, this is actually a benefit because it ensures that the
        user can't shut down merely the PromotedAi instance on accident, but is only ever calling
        the PromotedAiManager.shutdown() function when calling this companion object's inherited
        shutdown() function. This will ensure Koin and any other supporting classes are
        stopped/cleaned up.

        So, one can consider the PromotedAi interface's shutdown() function as merely there to
        serve as a gateway to the PromotedAiManager.shutdown() function, via this companion object's
        extension of both of those abstract classes. And since this companion object extends from
        both, that means the user gets a simple and single entry point for shutting down the
        library, e.g. by calling the statically available PromotedAi.shutdown().
     */
    @Suppress("ClassName")
    companion object instance : PromotedAiManager(), PromotedAi {
        override fun startSession(userId: String) = super.promotedAiInstance.startSession(userId)
        override fun onViewVisible(key: String) = super.promotedAiInstance.onViewVisible(key)
        override fun onAction(
            name: String,
            type: ActionType,
            dataBlock: (ActionData.Builder.() -> Unit)?
        ) = super.promotedAiInstance.onAction(name, type, dataBlock)

        override fun onAction(name: String, type: ActionType, data: ActionData) =
            super.promotedAiInstance.onAction(name, type, data)

        override fun onCollectionVisible(
            collectionViewKey: String,
            content: List<AbstractContent>
        ) = super.promotedAiInstance.onCollectionVisible(collectionViewKey, content)

        override fun onCollectionHidden(collectionViewKey: String) =
            super.promotedAiInstance.onCollectionHidden(collectionViewKey)

        //region JAVA INTER-OP
        /* Java-idiomatic initialization of the SDK */
        fun buildConfiguration() = SdkBuilder(this)

        /* Default parameter / signed out user */
        fun startSession() = super.promotedAiInstance.startSession()

        /* Java-idiomatic building of action/action data*/
        fun buildAction() = ActionBuilder(super.promotedAiInstance)
        //endregion
    }
}
