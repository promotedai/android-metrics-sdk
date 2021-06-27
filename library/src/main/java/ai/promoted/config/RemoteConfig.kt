package ai.promoted.config

import ai.promoted.ClientConfig

/**
 * Represents all the SDK config options that can be dynamically / remotely configured. This should
 * simply be mapped over to a [ClientConfig] and passed to the SDK for reconfiguration.
 */
internal data class RemoteConfig(
    /**
     * Whether to collect metrics. When set to false, the library will operate in a stubbed fashion;
     * meaning, API calls are available to the library user, but nothing will happen.
     */
    val loggingEnabled: Boolean?,

    /**
     * The URL of the server to receive the metrics
     */
    val metricsLoggingUrl: String?,

    /**
     * The API key required to authorize with the server defined by [metricsLoggingUrl]
     */
    val metricsLoggingApiKey: String?,

    /**
     * The format of the data to be sent to the server at [metricsLoggingUrl]. Typically
     * [ClientConfig.MetricsLoggingWireFormat.Binary]
     */
    val metricsLoggingWireFormat: ClientConfig.MetricsLoggingWireFormat?,

    /**
     * How long the SDK should batch / queue up metrics before sending to the server. Default is
     * ten seconds.
     */
    val loggingFlushIntervalSeconds: Long?,

    /**
     * Whether the SDK should monitor its own performance and log results to the system log.
     */
    val xrayEnabled: Boolean?
) {
    companion object {
        fun empty() = RemoteConfig(
            null,
            null,
            null,
            null,
            null,
            null
        )
    }
}
