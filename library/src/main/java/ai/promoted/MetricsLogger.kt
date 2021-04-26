package ai.promoted

import java.util.*
import kotlin.concurrent.fixedRateTimer

class MetricsLogger internal constructor(
    config: ClientConfig
) {
    private val timer = fixedRateTimer(
        name = "promoted_metrics_logger",
        daemon = true,
        initialDelay = config.loggingFlushInterval,
        period = config.loggingFlushInterval,
        action = this::onInterval
    )

    private var logMessages = mutableListOf<Any>()

    // TODO
    fun log() {
        logMessages.add(Unit)
    }

    internal fun shutdown() {
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