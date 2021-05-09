package ai.promoted.di

import ai.promoted.BuildConfig
import ai.promoted.ClientConfig
import ai.promoted.metrics.MetricsLogger
import ai.promoted.metrics.id.IdGenerator
import ai.promoted.metrics.id.UuidGenerator
import ai.promoted.metrics.usecases.CurrentUserIdsUseCase
import ai.promoted.metrics.usecases.FinalizeLogsUseCase
import ai.promoted.metrics.usecases.ImpressionIdGenerator
import ai.promoted.metrics.usecases.TrackActionUseCase
import ai.promoted.metrics.usecases.TrackImpressionsUseCase
import ai.promoted.metrics.usecases.TrackRVImpressionsUseCase
import ai.promoted.metrics.usecases.TrackSessionUseCase
import ai.promoted.metrics.usecases.TrackViewUseCase
import ai.promoted.platform.AndroidDeviceInfoProvider
import ai.promoted.platform.Clock
import ai.promoted.platform.DeviceInfoProvider
import ai.promoted.platform.KeyValueStorage
import ai.promoted.platform.LogcatLogger
import ai.promoted.platform.SharedPrefsKeyValueStorage
import ai.promoted.platform.SystemClock
import ai.promoted.platform.SystemLogger
import ai.promoted.sdk.DefaultSdk
import ai.promoted.sdk.PromotedAiSdk
import ai.promoted.xray.DefaultXray
import ai.promoted.xray.Xray
import android.content.Context
import android.content.SharedPreferences
import org.koin.core.module.Module
import org.koin.core.scope.Scope
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

/**
 * The default [ConfigurableKoinComponent] used at runtime of the library. Knows how to provide
 * all library dependencies taking the [ClientConfig] into account.
 */
internal object DefaultKoinComponent : ConfigurableKoinComponent() {
    override fun buildModules(config: ClientConfig): List<Module> = listOf(
        module {
            single<SystemLogger> { LogcatLogger(tag = "Promoted.Ai", verbose = BuildConfig.DEBUG) }
            single { config }
            single<PromotedAiSdk> { DefaultSdk(get(), get(), get(), get(), get(), get()) }
            single { createMetricsLoggerForConfig() }
            single { TrackSessionUseCase(get(), get(), get(), get(), get()) }
            single { TrackViewUseCase(get(), get(), get(), get(), get(), get()) }
            single { TrackImpressionsUseCase(get(), get(), get(), get(), get(), get()) }
            single { TrackRVImpressionsUseCase(get(), get()) }
            single { CurrentUserIdsUseCase(get()) }
            single<Xray> { DefaultXray(get(), get()) }

            factory { FinalizeLogsUseCase(get(), get(), get(), get()) }
            factory { TrackActionUseCase(get(), get(), get(), get(), get(), get(), get()) }

            factory { ImpressionIdGenerator(get(), get()) }

            factory<IdGenerator> { UuidGenerator() }
            factory<KeyValueStorage> { SharedPrefsKeyValueStorage(get()) }
            factory { getPromotedAiPrefs(get()) }

            factory<Clock> { SystemClock() }
            factory<DeviceInfoProvider> { AndroidDeviceInfoProvider() }
        }
    )

    private fun Scope.createMetricsLoggerForConfig(): MetricsLogger {
        val config: ClientConfig = get()
        val flushIntervalMillis =
            TimeUnit.SECONDS.toMillis(config.loggingFlushIntervalSeconds)
        val networkConnection = config.networkConnectionProvider()
        return MetricsLogger(flushIntervalMillis, networkConnection, get(), get())
    }

    private fun getPromotedAiPrefs(context: Context): SharedPreferences =
        context.getSharedPreferences("ai.promoted.prefs", Context.MODE_PRIVATE)
}
