package ai.promoted.metrics

import kotlinx.coroutines.*
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
    private var job: Job? = null

    private var scheduled = false
    private val timer = Timer()

    /**
     * If the provided operation has not yet been scheduled, schedule it
     */
    fun maybeSchedule() {
//        if (job != null) return
//        else job = scheduleOperation()
        if(scheduled) return
        timer.schedule(
            delay = intervalMillis,
            action = {
                operation.invoke()
                scheduled = false
            }
        )
    }

    /**
     * If the provided operation has been scheduled, cancel the pending execution of the operation
     */
    fun cancel() {
//        job?.cancel()
//        job = null
        timer.cancel()
        scheduled = false
    }

    private fun scheduleOperation() = GlobalScope.launch {
        delay(intervalMillis)
        if (isActive) operation.invoke()
        job = null
    }
}