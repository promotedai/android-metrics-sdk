package ai.promoted

import ai.promoted.networking.NetworkConnection
import android.app.Application

/**
 * This class is primarily to provide an easy-to-use Java API, via [PromotedAiSdk.buildConfiguration],
 * so that users of the library can dynamically set whichever [ClientConfig] options they want to
 * set.
 *
 * While this is supported for Kotlin users, the [PromotedAiSdk.initialize]/[PromotedAiSdk.configure]
 * with the [ClientConfig.Builder] configuration block is recommended.
 */
class SdkBuilder internal constructor(
    private val promotedAiManager: PromotedAiManager
) {
    interface NetworkConnectionProvider {
        /**
         * @see [ClientConfig.networkConnectionProvider]
         */
        fun provide(): NetworkConnection
    }

    private val clientConfigBuilder = ClientConfig.Builder()

    /**
     * @see [ClientConfig.loggingEnabled]
     */
    fun withLoggingEnabled(enabled: Boolean) =
        apply { clientConfigBuilder.loggingEnabled = enabled }

    /**
     * @see [ClientConfig.metricsLoggingUrl]
     */
    fun withMetricsLoggingUrl(url: String) =
        apply { clientConfigBuilder.metricsLoggingUrl = url }

    /**
     * @see [ClientConfig.metricsLoggingApiKey]
     */
    fun withMetricsLoggingApiKey(apiKey: String) =
        apply { clientConfigBuilder.metricsLoggingApiKey = apiKey }

    /**
     * @see [ClientConfig.metricsLoggingWireFormat]
     */
    fun withMetricsLoggingWireFormat(format: ClientConfig.MetricsLoggingWireFormat) =
        apply { clientConfigBuilder.metricsLoggingWireFormat = format }

    /**
     * @see [ClientConfig.loggingFlushIntervalSeconds]
     */
    fun withLoggingFlushIntervalSeconds(intervalSeconds: Long) =
        apply { clientConfigBuilder.loggingFlushIntervalSeconds = intervalSeconds }

    /**
     * @see [ClientConfig.networkConnectionProvider]
     */
    fun withNetworkConnectionProvider(provider: NetworkConnectionProvider) =
        apply { clientConfigBuilder.networkConnectionProvider = provider::provide }

    /**
     * @see [PromotedAiSdk.initialize]
     */
    fun initialize(application: Application) = configure(application)

    /**
     * @see [PromotedAiSdk.configure]
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun configure(application: Application) =
        promotedAiManager.configure(application, clientConfigBuilder.build())
}
