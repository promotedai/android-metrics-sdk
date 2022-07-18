package ai.promoted.metrics.usecases.anomaly

import ai.promoted.ClientConfig
import android.content.Context

internal class ModalDialogAnomalyHandler(
    private val context: Context,
    private val anomalyContactInfo: ClientConfig.LoggingAnomalyContactInfo
) : AnomalyHandler {
    override fun handle(anomalyType: AnomalyType) =
        ModalAnomalyActivity.show(context, anomalyType, anomalyContactInfo)
}