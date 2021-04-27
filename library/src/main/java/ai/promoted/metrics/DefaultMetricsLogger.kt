package ai.promoted.metrics

import ai.promoted.ClientConfig
import java.util.*
import kotlin.concurrent.fixedRateTimer

class DefaultMetricsLogger internal constructor(
    config: ClientConfig
) : MetricsLogger {
    private val timer = fixedRateTimer(
        name = "promoted_metrics_logger",
        daemon = true,
        initialDelay = config.loggingFlushInterval,
        period = config.loggingFlushInterval,
        action = this::onInterval
    )

    private var logMessages = mutableListOf<Any>()

    // TODO
    override fun log() {
        logMessages.add(Unit)
    }

    override fun shutdown() {
        timer.cancel()
    }

    private fun onInterval(timerTask: TimerTask) {
        /*
            TODO:
            - Send current list
            - Reset list to new mutableList
            -
         */
    }
}