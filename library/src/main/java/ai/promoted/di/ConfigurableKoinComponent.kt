package ai.promoted.di

import ai.promoted.ClientConfig
import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module

/**
 * Allows you to dynamically configure your Koin DI/service location based on the provided
 * [ClientConfig]. For example, dependencies my need to be re-constructed with new values if the
 * [ClientConfig] has changed.
 */
internal abstract class ConfigurableKoinComponent : KoinComponent {
    protected var startedKoinApplication: KoinApplication? = null

    protected abstract fun buildModules(config: ClientConfig): List<Module>

    /**
     * Stops any existing Koin instances and re-starts it based on the new [ClientConfig]
     */
    fun configure(application: Application, config: ClientConfig) {
        stopKoinIfStarted()
        startedKoinApplication = startKoin {
            androidContext(application)
            modules(buildModules(config))
        }
    }

    /**
     * Stops Koin if it is running
     */
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
