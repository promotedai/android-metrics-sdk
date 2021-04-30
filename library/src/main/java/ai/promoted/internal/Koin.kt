package ai.promoted.internal

import ai.promoted.ClientConfig
import ai.promoted.DefaultPromotedAi
import ai.promoted.PromotedAi
import ai.promoted.metrics.MetricsLogger
import ai.promoted.metrics.StartSessionUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.dsl.module

internal abstract class ConfigurableKoinComponent : KoinComponent {
    private var alreadyInitialized = false

    protected abstract fun buildModules(config: ClientConfig): List<Module>

    fun configure(config: ClientConfig) {
        if (alreadyInitialized) stopKoin()
        startKoin { modules(buildModules(config)) }
        alreadyInitialized = true
    }
}

internal object DefaultKoin : ConfigurableKoinComponent() {
    override fun buildModules(config: ClientConfig): List<Module> = listOf(
        module {
            single { config }
            single<PromotedAi> { DefaultPromotedAi(get(), get(), get()) }
            single { MetricsLogger(get()) }
            factory { StartSessionUseCase(get()) }
        }
    )
}