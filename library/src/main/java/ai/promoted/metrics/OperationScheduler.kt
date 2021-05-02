package ai.promoted.metrics

import java.util.*
import kotlin.concurrent.schedule

/**
 * Given an interval, uses a privately managed coroutine scope & job to schedule future execution
 * of the provided operation.
 */
internal class OperationScheduler(
    private val intervalMillis: Long,
    private val operation: () -> Unit
) {
    private var scheduled = false
    private val timer = Timer()

    /**
     * If the provided operation has not yet been scheduled, schedule it
     */
    fun maybeSchedule() {
        if (scheduled) return
         scheduleOperation()
    }

    /**
     * If the provided operation has been scheduled, cancel the pending execution of the operation
     */
    fun cancel() {
        timer.cancel()
        scheduled = false
    }

    private fun scheduleOperation() = timer.schedule(
        delay = intervalMillis,
        action = {
            operation.invoke()
            scheduled = false
        }
    ).let { scheduled = true }
}
