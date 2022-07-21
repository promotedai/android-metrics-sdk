package ai.promoted.metrics.usecases.anomaly

import ai.promoted.ClientConfig
import ai.promoted.R
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.TextView

/**
 * Presents a modal with detailed error message when the SDK has not been initialized
 * properly. Clients should not use this class directly.
 *
 * Although this class is intended only for internal use in the Promoted metrics
 * library, it is declared public so that ReactNativeMetrics can show the modal
 * for module initialization issues.
 */
class ModalAnomalyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeOrFinish()
    }

    private fun initializeOrFinish() {
        val anomalyType = intent.extras?.readAnomalyType()
        val anomalyContactInfo = intent.extras?.readAnomalyContactInfo()

        if (anomalyType == null || anomalyContactInfo == null) finish()
        else initializeUi(anomalyType, anomalyContactInfo)
    }

    private fun initializeUi(
        anomalyType: AnomalyType,
        anomalyContactInfo: ClientConfig.LoggingAnomalyContactInfo
    ) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_anomaly)
        findViewById<TextView>(R.id.message).text =
            anomalyType.buildErrorMessage(anomalyContactInfo)
        findViewById<View>(R.id.dontShowAgainButton).setOnClickListener {
            onDontShowAgainClicked()
        }
        findViewById<View>(R.id.closeButton).setOnClickListener {
            onCloseClicked()
        }
    }

    private fun AnomalyType.buildErrorMessage(anomalyContactInfo: ClientConfig.LoggingAnomalyContactInfo) =
        """
            $debugDescription
            
            Error code: $errorCode
            
            For more help, please contact Promoted:
              ${anomalyContactInfo.slackInfo()}
              - Email: ${anomalyContactInfo.email.value}
              
            This warning will only appear in development builds.
        """.trimIndent()

    private fun ClientConfig.LoggingAnomalyContactInfo.slackInfo() =
        if (this.slack == null) ""
        else {
            """
              - Slack: ${this.slack.value}
            """.trimIndent()
        }

    private fun onDontShowAgainClicked() {
        ModalDialogAnomalyHandler.handlingDismissedForSession = true
        finish()
    }

    private fun onCloseClicked() = finish()

    companion object {
        private const val ANOMALY_TYPE = "anomaly_type"
        private const val ANOMALY_CONTACT_SLACK = "anomaly_contact_slack"
        private const val ANOMALY_CONTACT_EMAIL = "anomaly_contact_email"

        /**
         * Presents a modal with detailed error message when the SDK has not been initialized
         * properly. Clients should not use this function directly.
         *
         * Although this function is intended only for internal use in the Promoted metrics
         * library, it is declared public so that ReactNativeMetrics can show the modal
         * for module initialization issues.
         */
        @JvmStatic
        fun showInitializationAnomaly(
            context: Context,
            slack: String?,
            email: String
        ) {
            val slackInfo = slack?.let { ClientConfig.LoggingAnomalyContactInfo.Slack(it) }
            show(
                context, AnomalyType.SdkNotInitialized, ClientConfig.LoggingAnomalyContactInfo(
                    slackInfo, ClientConfig.LoggingAnomalyContactInfo.Email(email)
                )
            )
        }

        internal fun show(
            context: Context,
            anomalyType: AnomalyType,
            anomalyContactInfo: ClientConfig.LoggingAnomalyContactInfo
        ) =
            context.startActivity(
                Intent(context, ModalAnomalyActivity::class.java)
                    .apply {
                        putExtras(Bundle().apply {
                            putAnomalyType(anomalyType)
                            putAnomalyContactInfo(anomalyContactInfo)
                        })

                        // Ensure this activity can be started from outside the context of
                        // another activity
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
            )

        private fun Bundle.putAnomalyType(anomalyType: AnomalyType) =
            this.putString(ANOMALY_TYPE, anomalyType.name)

        private fun Bundle.readAnomalyType(): AnomalyType? =
            getString(ANOMALY_TYPE)?.let {
                AnomalyType.valueOf(
                    it
                )
            }

        private fun Bundle.putAnomalyContactInfo(anomalyContactInfo: ClientConfig.LoggingAnomalyContactInfo) =
            this.apply {
                anomalyContactInfo.slack?.let {
                    putString(
                        ANOMALY_CONTACT_SLACK,
                        anomalyContactInfo.slack.value
                    )
                }
                putString(ANOMALY_CONTACT_EMAIL, anomalyContactInfo.email.value)
            }

        private fun Bundle.readAnomalyContactInfo(): ClientConfig.LoggingAnomalyContactInfo {
            val slack = getString(ANOMALY_CONTACT_SLACK)
            val email = getString(ANOMALY_CONTACT_EMAIL)
            return if (email.isNullOrBlank()) ClientConfig.LoggingAnomalyContactInfo.default
            else ClientConfig.LoggingAnomalyContactInfo(
                slack?.let { ClientConfig.LoggingAnomalyContactInfo.Slack(it) },
                ClientConfig.LoggingAnomalyContactInfo.Email(email)
            )
        }
    }
}
