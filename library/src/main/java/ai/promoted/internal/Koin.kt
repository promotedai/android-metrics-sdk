package ai.promoted.internal

import ai.promoted.ClientConfig
import ai.promoted.DefaultPromotedAi
import ai.promoted.PromotedAi
import ai.promoted.metrics.MetricsLogger
import ai.promoted.metrics.id.IdGenerator
import ai.promoted.metrics.id.UuidGenerator
import ai.promoted.metrics.storage.IdStorage
import ai.promoted.metrics.storage.PrefsIdStorage
import ai.promoted.metrics.usecases.IdStorageUseCase
import ai.promoted.metrics.usecases.LogSessionUseCase
import ai.promoted.metrics.usecases.LogUserUseCase
import ai.promoted.metrics.usecases.StartSessionUseCase
import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.dsl.module

internal abstract class ConfigurableKoinComponent : KoinComponent {
    private var alreadyInitialized = false

    protected abstract fun buildModules(config: ClientConfig): List<Module>

    fun configure(application: Application, config: ClientConfig) {
        if (alreadyInitialized) stopKoin()
        startKoin {
            androidContext(application)
            modules(buildModules(config))
        }
        alreadyInitialized = true
    }
}

internal object DefaultKoin : ConfigurableKoinComponent() {
    override fun buildModules(config: ClientConfig): List<Module> = listOf(
        module {
            single { config }
            single<PromotedAi> { DefaultPromotedAi(get(), get(), get()) }
            single { MetricsLogger(get()) }
            single { StartSessionUseCase(get(), get(), get(), get()) }
            single { IdStorageUseCase(get()) }
            factory { LogUserUseCase(get()) }
            factory { LogSessionUseCase(get()) }

            factory<IdGenerator> { UuidGenerator() }
            factory<IdStorage> { PrefsIdStorage(get()) }
            factory { SharedPreferencesProvider.default(get()) }
        }
    )
}