package ai.promoted

import ai.promoted.internal.ConfigurableKoinComponent
import ai.promoted.internal.DefaultKoin
import ai.promoted.metrics.MetricsLogger
import ai.promoted.metrics.usecases.StartSessionUseCase
import android.app.Application
import org.koin.core.component.get

abstract class PromotedAiManager internal constructor(
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
            State.Shutdown -> throw IllegalStateException("PromotedAi was shut down. Please ensure it is configured before usage again.")
            is State.Ready -> currentState.promotedAi
        }


    /**
     * Simply calls [configure], but provides semantic clarity for users of Promoted.Ai. For example,
     * you might call this function, [initialize] in your application onCreate(), but if you want
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

    fun shutdown() {
        instance.shutdown()
        state = State.Shutdown
    }
}

interface PromotedAi {
    fun startSession(userId: String = "")
    fun shutdown()

    companion object : PromotedAiManager(), PromotedAi {
        override fun startSession(userId: String) = instance.startSession(userId)
    }
}

internal class NoOpPromotedAi : PromotedAi {
    override fun startSession(userId: String) {}
    override fun shutdown() {}
}

internal class DefaultPromotedAi(
    private val config: ClientConfig,
    private val logger: MetricsLogger,
    private val startSessionUseCase: StartSessionUseCase
) : PromotedAi {
    override fun startSession(userId: String) = startSessionUseCase.startSession(userId)
    override fun shutdown() = logger.cancelAndDiscardPendingQueue()
}