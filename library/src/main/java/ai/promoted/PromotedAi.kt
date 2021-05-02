package ai.promoted

import ai.promoted.internal.ConfigurableKoinComponent
import ai.promoted.internal.DefaultKoin
import ai.promoted.metrics.MetricsLogger
import ai.promoted.metrics.usecases.TrackSessionUseCase
import ai.promoted.metrics.usecases.TrackViewUseCase
import android.app.Application
import org.koin.core.component.get

/**
 * Allows for the proper creation, configuration, and termination of a [PromotedAi]
 * instance. This class ensures that an instance of [PromotedAi] is able to be initialized with
 * a [ClientConfig], reconfigured at any point, and also shut down at any point by the user of
 * [PromotedAi]. It ensures that when a [PromotedAi] is initialized or reconfigured, its
 * corresponding objects/dependencies are re-created per the new [ClientConfig]. It also ensures
 * that when [PromotedAi] is shut down, its corresponding objects/dependencies are released for
 * garbage collection.
 */
open class PromotedAiManager internal constructor(
    private val configurableKoinComponent: ConfigurableKoinComponent = DefaultKoin
) {
    internal sealed class State {
        object NotConfigured : State()
        data class Ready(val promotedAi: PromotedAi) : State()
        object Shutdown : State()
    }

    private var state: State = State.NotConfigured

    internal val instance: PromotedAi
        get() = when (val currentState = state) {
            is State.NotConfigured -> throw IllegalStateException("Please configure PromotedAi before use")
            State.Shutdown -> throw IllegalStateException(
                "PromotedAi was shut down. Please ensure it is configured before usage again."
            )
            is State.Ready -> currentState.promotedAi
        }


    /**
     * Simply calls [configure], but provides semantic clarity for users of Promoted.Ai. For example,
     * you might call this function, [initialize], in your application onCreate(), but if you want
     * to reconfigure at a later point, it would be clearer if you called [configure].
     */
    fun initialize(application: Application, block: ClientConfig.Builder.() -> Unit) =
        configure(application, block)

    /**
     * Initializes (or reconfigures) Promoted.Ai with the given configuration. Subsequent calls
     * after the initial call will simply reconfigure & restart Promoted.Ai
     */
    fun configure(application: Application, block: ClientConfig.Builder.() -> Unit) {
        // Shut down the current PromotedAi instance, if there is one running / we're in a ready
        // state
        when (val currentState = state) {
            is State.Ready -> currentState.promotedAi.shutdown()
        }

        // Reconfigure Koin to provide dependencies based on the current ClientConfig
        val config = ClientConfig.Builder().apply(block).build()
        configurableKoinComponent.configure(application, config)

        // Regardless of what PromotedAi type Koin might return, we'll always override that with a
        // no-op version if logging is disabled via config. This is to prevent such critical
        // business logic from residing in the DI configuration
        val newPromotedAi: PromotedAi = when (config.loggingEnabled) {
            true -> configurableKoinComponent.get()
            else -> NoOpPromotedAi()
        }

        this.state = State.Ready(promotedAi = newPromotedAi)
    }

    /**
     * Cancels any pending metrics that have yet to be sent, and puts the library in a dormant
     * state.
     */
    fun shutdown() {
        instance.shutdown()
        configurableKoinComponent.shutdown()
        state = State.Shutdown
    }
}

/**
 * The public-facing API for interacting with Promoted.Ai. Instances are managed internally by
 * the SDK.
 */
interface PromotedAi {
    fun startSession(userId: String = "")
    fun onViewVisible(key: String)
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
    companion object : PromotedAiManager(), PromotedAi {
        override fun startSession(userId: String) = instance.startSession(userId)
        override fun onViewVisible(key: String) = instance.onViewVisible(key)
    }
}

@Suppress("EmptyFunctionBlock")
internal class NoOpPromotedAi : PromotedAi {
    override fun startSession(userId: String) {}
    override fun onViewVisible(key: String) {}
    override fun shutdown() {}
}

internal class DefaultPromotedAi(
    private val logger: MetricsLogger,
    private val trackSessionUseCase: TrackSessionUseCase,
    private val trackViewUseCase: TrackViewUseCase
) : PromotedAi {
    override fun startSession(userId: String) = trackSessionUseCase.startSession(userId)
    override fun onViewVisible(key: String) = trackViewUseCase.onViewVisible(key)
    override fun shutdown() = logger.cancelAndDiscardPendingQueue()
}
