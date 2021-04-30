package ai.promoted.internal

import ai.promoted.ClientConfig
import ai.promoted.DefaultPromotedAi
import ai.promoted.PromotedAi
import ai.promoted.metrics.MetricsLogger
import ai.promoted.metrics.id.IdGenerator
import ai.promoted.metrics.id.UuidGenerator
import ai.promoted.metrics.storage.IdStorage
import ai.promoted.metrics.storage.PrefsIdStorage
import ai.promoted.metrics.usecases.*
import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.core.scope.Scope
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

internal abstract class ConfigurableKoinComponent : KoinComponent {
    protected var startedKoinApplication: KoinApplication? = null

    protected abstract fun buildModules(config: ClientConfig): List<Module>

    fun configure(application: Application, config: ClientConfig) {
        stopKoinIfStarted()
        startedKoinApplication = startKoin {
            androidContext(application)
            modules(buildModules(config))
        }
    }

    fun shutdown() = stopKoinIfStarted()

    private fun stopKoinIfStarted() = when (startedKoinApplication) {
        null -> {
        }
        else -> {
            stopKoin()
            startedKoinApplication = null
        }
    }
}

internal object DefaultKoin : ConfigurableKoinComponent() {
    override fun buildModules(config: ClientConfig): List<Module> = listOf(
        module {
            single { config }
            single<PromotedAi> { DefaultPromotedAi(get(), get(), get()) }
            single { createMetricsLoggerForConfig() }
            single { StartSessionUseCase(get(), get(), get(), get()) }
            single { IdStorageUseCase(get()) }

            factory { LogUserUseCase(get(), get()) }
            factory { LogSessionUseCase(get()) }
            factory { FinalizeLogsUseCase(get()) }

            factory<IdGenerator> { UuidGenerator() }
            factory<IdStorage> { PrefsIdStorage(get()) }
            factory { SharedPreferencesProvider.default(get()) }

            factory<Clock> { SystemClock() }
        }
    )

    private fun Scope.createMetricsLoggerForConfig(): MetricsLogger {
        val config: ClientConfig = get()
        val flushIntervalMillis =
            TimeUnit.SECONDS.toMillis(config.loggingFlushIntervalSeconds)
        val networkConnection = config.networkConnectionProvider()
        return MetricsLogger(flushIntervalMillis, networkConnection, get())
    }
}