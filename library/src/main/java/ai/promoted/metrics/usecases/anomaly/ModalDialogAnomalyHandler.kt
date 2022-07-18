package ai.promoted.metrics.usecases.anomaly

import ai.promoted.ClientConfig
import android.content.Context

internal class ModalDialogAnomalyHandler(
    private val context: Context,
    private val anomalyContactInfo: ClientConfig.LoggingAnomalyContactInfo
) : AnomalyHandler {
    override fun handle(anomalyType: AnomalyType) {
        if (!handlingDismissedForSession)
            ModalAnomalyActivity.show(context, anomalyType, anomalyContactInfo)
    }

    internal companion object {
        /**
         * Shared boolean to determine if the developer using the library
         * has opted to dismiss anomaly dialogs for the length of the app
         * session.
         */
        @JvmStatic
        internal var handlingDismissedForSession = false
    }
}