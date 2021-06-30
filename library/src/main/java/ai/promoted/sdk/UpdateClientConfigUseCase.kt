package ai.promoted.sdk

import ai.promoted.ClientConfig
import ai.promoted.config.RemoteConfig
import ai.promoted.config.RemoteConfigServiceFinder

/**
 * The purpose of this class is to take the latest cached [RemoteConfig] and merge it with the
 * compiled [ClientConfig] that the library user is passing in. The output will be a new
 * [ClientConfig] that prefers values from the [RemoteConfig] (since it's dynamic) over the original
 * baseline/compiled/hard-coded [ClientConfig] used to initialize the SDK.
 *
 * Any remotely configurable values on the original [ClientConfig] will be overridden by their
 * [RemoteConfig] counterparts if they are non-null in the latest [RemoteConfig].
 */
internal class UpdateClientConfigUseCase(
    remoteConfigServiceFinder: RemoteConfigServiceFinder
) {
    private val service = remoteConfigServiceFinder.findAvailableService()

    /**
     * Ensure any [RemoteConfig] values are applied on top of this baseline [ClientConfig].
     */
    fun updateClientConfig(baselineConfig: ClientConfig): ClientConfig {
        /* Kick off a remote-config update that, if updated, will apply to the *next* session */
        service.fetchLatestConfigValues()

        // Get the currently cached remote config and apply it on top of the compiled/baseline
        // ClientConfig
        val latestRemoteConfig = service.latestRemoteConfig

        return mergeRemoteWithBaseline(latestRemoteConfig, baselineConfig)
    }

    /*
        Copy the baseline, but overwrite any values if the RemoteConfig has a non-null for the
        corresponding value.
     */
    private fun mergeRemoteWithBaseline(
        remoteConfig: RemoteConfig,
        baselineConfig: ClientConfig
    ): ClientConfig = baselineConfig.copy(
        loggingEnabled = remoteConfig.loggingEnabled ?: baselineConfig.loggingEnabled,
        metricsLoggingUrl = remoteConfig.metricsLoggingUrl ?: baselineConfig.metricsLoggingUrl,
        metricsLoggingApiKey = remoteConfig.metricsLoggingApiKey
            ?: baselineConfig.metricsLoggingApiKey,
        metricsLoggingWireFormat = remoteConfig.metricsLoggingWireFormat
            ?: baselineConfig.metricsLoggingWireFormat,
        loggingFlushIntervalSeconds = remoteConfig.loggingFlushIntervalSeconds
            ?: baselineConfig.loggingFlushIntervalSeconds,
        xrayEnabled = remoteConfig.xrayEnabled ?: baselineConfig.xrayEnabled
    )
}
