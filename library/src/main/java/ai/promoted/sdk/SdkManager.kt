package ai.promoted.sdk

import ai.promoted.ClientConfig
import ai.promoted.di.ConfigurableKoinComponent
import ai.promoted.di.DefaultKoinComponent
import android.app.Application
import org.koin.core.component.get

/**
 * Allows for the proper creation, configuration, and termination of a [PromotedAiSdk]
 * instance. This class ensures that an instance of [PromotedAiSdk] is able to be initialized with
 * a [ClientConfig], reconfigured at any point, and also shut down at any point by the user of
 * [PromotedAiSdk]. It ensures that when a [PromotedAiSdk] is initialized or reconfigured, its
 * corresponding objects/dependencies are re-created per the new [ClientConfig]. It also ensures
 * that when [PromotedAiSdk] is shut down, its corresponding objects/dependencies are released for
 * garbage collection.
 */
internal open class SdkManager internal constructor(
    private val configurableKoinComponent: ConfigurableKoinComponent = DefaultKoinComponent
) {
    internal sealed class SdkState {
        object NotConfigured : SdkState()
        data class Ready(val sdk: PromotedAiSdk) : SdkState()
        object Shutdown : SdkState()
    }

    private var sdkState: SdkState = SdkState.NotConfigured

    internal val sdkInstance: PromotedAiSdk
        get() = when (val currentState = sdkState) {
            is SdkState.NotConfigured,
            SdkState.Shutdown -> {
                // Rather than throwing an IllegalStateException here, we'll just return a no-op
                // instance. That way, if the library user has called shutdown() for some reason at
                // runtime, it won't be up to them to ensure all the places in their code where
                // they access Promoted.Ai are wrapped with logic to ensure initialize/configure
                // has been called.
                NoOpSdk()
            }
            is SdkState.Ready -> currentState.sdk
        }


    /**
     * Same functionality as [initialize] with a [ClientConfig], except this makes use of
     * Kotlin-specific language features to allow a more idiomatic expression of your Promoted.Ai
     * configuration. For example, rather than manually constructing a [ClientConfig] object and
     * passing it into [initialize]/[configure], this function allows you to declare only the
     * config-values you wish to customize, while leaving the rest to defaults. So, instead of
     * something like this:
     *
     * val config = ClientConfig(value1, value2, value3, value4, value5);
     * PromotedAi.initialize(application, config)
     *
     * you can do something like this:
     *
     * PromotedAi.initialize(application) {
     *     // Only the values I'm interested in
     *     value2 = "value 2"
     *     value4 = "value 4"
     * }
     *
     * For more in-depth understanding of how this Kotlin feature works, see:
     * https://kotlinlang.org/docs/lambdas.html#function-literals-with-receiver
     */
    fun initialize(application: Application, block: ClientConfig.Builder.() -> Unit) =
        configure(application, block)

    /**
     * Same as calling the Kotlin-idiomatic [initialize] with a custom configuration lambda.
     */
    fun configure(application: Application, block: ClientConfig.Builder.() -> Unit) =
        configure(application = application, config = ClientConfig.Builder().apply(block).build())

    /**
     * Simply calls [configure], but provides semantic clarity for users of Promoted.Ai. For example,
     * you might call this function, [initialize], in your application onCreate(), but if you want
     * to reconfigure at a later point, it would be clearer if you called [configure].
     */
    fun initialize(application: Application, config: ClientConfig) =
        configure(application, config)

    /**
     * Initializes (or reconfigures) Promoted.Ai with the given configuration. Subsequent calls
     * after the initial call will simply reconfigure & restart Promoted.Ai
     */
    fun configure(application: Application, config: ClientConfig) {
        // Shut down the current PromotedAi instance, if there is one running / we're in a ready
        // state
        when (val currentState = sdkState) {
            is SdkState.Ready -> currentState.sdk.shutdown()
        }

        // Reconfigure Koin to provide dependencies based on the current ClientConfig
        configurableKoinComponent.configure(application, config)

        // Regardless of what PromotedAi type Koin might return, we'll always override that with a
        // no-op version if logging is disabled via config. This is to prevent such critical
        // business logic from residing in the DI configuration
        val newPromotedAi: PromotedAiSdk = when (config.loggingEnabled) {
            true -> configurableKoinComponent.get()
            else -> NoOpSdk()
        }

        this.sdkState = SdkState.Ready(sdk = newPromotedAi)
    }

    /**
     * Cancels any pending metrics that have yet to be sent, and puts the library in a dormant
     * state.
     */
    fun shutdown() {
        sdkInstance.shutdown()
        configurableKoinComponent.shutdown()
        sdkState = SdkState.Shutdown
    }
}
