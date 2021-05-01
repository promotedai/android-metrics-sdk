package ai.promoted.internal

import ai.promoted.BuildConfig
import ai.promoted.ClientConfig
import ai.promoted.DefaultPromotedAi
import ai.promoted.PromotedAi
import ai.promoted.metrics.MetricsLogger
import ai.promoted.metrics.id.IdGenerator
import ai.promoted.metrics.id.UuidGenerator
import ai.promoted.metrics.storage.PrefsUserIdStorage
import ai.promoted.metrics.storage.UserIdStorage
import ai.promoted.metrics.usecases.CurrentUserIdsUseCase
import ai.promoted.metrics.usecases.FinalizeLogsUseCase
import ai.promoted.metrics.usecases.TrackSessionUseCase
import ai.promoted.metrics.usecases.TrackViewUseCase
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
            single<SystemLogger> { LogcatLogger(tag = "Promoted.Ai", verbose = BuildConfig.DEBUG) }
            single { config }
            single<PromotedAi> { DefaultPromotedAi(get(), get(), get()) }
            single { createMetricsLoggerForConfig() }
            single { TrackSessionUseCase(get(), get(), get(), get()) }
            single { TrackViewUseCase(get(), get(), get(), get()) }
            single { CurrentUserIdsUseCase(get()) }

            factory { FinalizeLogsUseCase(get(), get(), get()) }

            factory<IdGenerator> { UuidGenerator() }
            factory<UserIdStorage> { PrefsUserIdStorage(get()) }
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