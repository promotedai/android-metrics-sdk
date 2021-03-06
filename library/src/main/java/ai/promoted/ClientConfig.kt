package ai.promoted

import ai.promoted.http.RetrofitNetworkConnection
import ai.promoted.http.RetrofitProvider
import ai.promoted.platform.AppRuntimeEnvironment

/**
 * Represents all of the options a library user has to customize the behavior of the Promoted.Ai
 * SDK.
 */
data class ClientConfig(
    /**
     * Whether to collect metrics. When set to false, the library will operate in a stubbed fashion;
     * meaning, API calls are available to the library user, but nothing will happen.
     */
    val loggingEnabled: Boolean,

    /**
     * How to handle logging anomalies.
     */
    val loggingAnomalyHandling: LoggingAnomalyHandling,

    /**
     * How to contact Promoted to assist in resolving anomalies
     */
    val loggingAnomalyContactInfo: LoggingAnomalyContactInfo,

    /**
     * The URL of the server to receive the metrics
     */
    val metricsLoggingUrl: String,

    /**
     * The API key required to authorize with the server defined by [metricsLoggingUrl]
     */
    val metricsLoggingApiKey: String,

    /**
     * The format of the data to be sent to the server at [metricsLoggingUrl]. Typically
     * [ClientConfig.MetricsLoggingWireFormat.Binary]
     */
    val metricsLoggingWireFormat: MetricsLoggingWireFormat,

    /**
     * How long the SDK should batch / queue up metrics before sending to the server. Default is
     * ten seconds.
     */
    val loggingFlushIntervalSeconds: Long,

    /**
     * Whether the SDK should monitor its own performance and log results to the system log.
     */
    val xrayEnabled: Boolean,

    /**
     * A function which provides the [NetworkConnection] to be used when sending metrics.
     * Promoted.Ai will provide a default implementation unless a custom one is provided by the
     * library user.
     */
    val networkConnectionProvider: () -> NetworkConnection,
) {
    /**
     * Supported formats for the metrics to be sent to the server.
     */
    enum class MetricsLoggingWireFormat {
        Json,
        Binary
    }

    /**
     * Different types of action to take upon a logging anomaly (e.g. bad event structure).
     */
    enum class LoggingAnomalyHandling {
        None,
        ConsoleLog,
        ModalDialog;

        companion object {
            /**
             * Based on the runtime environment, determine the default mechanism for handling
             * logging anomalies.
             */
            val default: LoggingAnomalyHandling
                get() {
                    val environment = AppRuntimeEnvironment.default
                    return when {
                        environment.isDebuggable -> ModalDialog
                        else -> None
                    }
                }
        }
    }

    /**
     * How a developer should reach out for support with logging anomalies
     */
    data class LoggingAnomalyContactInfo(
        val slack: Slack?,
        val email: Email
    ) {
        @JvmInline
        value class Slack(val value: String)

        @JvmInline
        value class Email(val value: String)

        companion object {
            val default = LoggingAnomalyContactInfo(
                slack = null,
                email = Email("help@promoted.ai")
            )
        }
    }

    data class Builder(
        /**
         * @see [ClientConfig.loggingEnabled]
         */
        var loggingEnabled: Boolean = true,

        /**
         * @see [ClientConfig.loggingAnomalyHandling]
         */
        var loggingAnomalyHandling: LoggingAnomalyHandling = LoggingAnomalyHandling.default,

        /**
         * @see [ClientConfig.loggingAnomalyContactInfo]
         */
        var loggingAnomalyContactInfo: LoggingAnomalyContactInfo = LoggingAnomalyContactInfo.default,

        /**
         * @see [ClientConfig.metricsLoggingUrl]
         */
        var metricsLoggingUrl: String = "",
        /**
         * @see [ClientConfig.metricsLoggingApiKey]
         */
        var metricsLoggingApiKey: String = "",
        /**
         * @see [ClientConfig.metricsLoggingWireFormat]
         */
        var metricsLoggingWireFormat: MetricsLoggingWireFormat = MetricsLoggingWireFormat.Binary,
        /**
         * @see [ClientConfig.loggingFlushIntervalSeconds]
         */
        var loggingFlushIntervalSeconds: Long = 10,
        /**
         * @see [ClientConfig.xrayEnabled]
         */
        var xrayEnabled: Boolean = false,
        /**
         * @see [ClientConfig.networkConnectionProvider]
         */
        var networkConnectionProvider: () -> NetworkConnection = {
            RetrofitNetworkConnection(
                RetrofitProvider()
            )
        }
    ) {
        /**
         * Create a [ClientConfig] object from the current state of this builder.
         */
        fun build() = ClientConfig(
            loggingEnabled,
            loggingAnomalyHandling,
            loggingAnomalyContactInfo,
            metricsLoggingUrl,
            metricsLoggingApiKey,
            metricsLoggingWireFormat,
            loggingFlushIntervalSeconds,
            xrayEnabled,
            networkConnectionProvider
        )
    }
}
