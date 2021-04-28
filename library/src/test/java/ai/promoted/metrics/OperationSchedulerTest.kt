package ai.promoted.metrics

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class OperationSchedulerTest {
    private val observableOperation = object {
        var executionCount = 0

        fun doOperation() {
            println("Executing @ ${System.currentTimeMillis()}")
            executionCount++
        }
    }

    // Assumes the scheduled operation will take no longer than 10ms to return
    // Can be increased to whatever is needed, but should be small since the
    // operation should not be long-running/blocking
    private val Long.withTimeForExecution
        get() = this + 10

    @Test
    fun `Should invoke operation only once after specified interval`() = runBlocking {
        // Given that an operation has been scheduled to execute after the interval below
        val interval = 100L
        val scheduler = OperationScheduler(
            intervalMillis = interval,
            operation = observableOperation::doOperation
        )
        scheduler.maybeSchedule()

        // When the interval has passed
        delay(interval.withTimeForExecution)

        // Then the operation ran
        // and even as time goes on, the operation has only been invoked once
        assertThat(observableOperation.executionCount, equalTo(1))
        delay(interval)
        delay(interval)
        assertThat(observableOperation.executionCount, equalTo(1))
    }

    @Test
    fun `Should not invoke operation at all when canceled before interval reached`() = runBlocking {
        // Given that an operation has been scheduled to execute after the interval below
        val interval = 100L
        val scheduler = OperationScheduler(
            intervalMillis = interval,
            operation = observableOperation::doOperation
        )
        scheduler.maybeSchedule()

        // When only half the interval has passed
        // and the operation was canceled
        delay(interval / 2)
        scheduler.cancel()

        // Then the operation is never invoked, even after the full interval has passed
        // several times
        assertThat(observableOperation.executionCount, equalTo(0))

        delay(interval)
        delay(interval)

        assertThat(observableOperation.executionCount, equalTo(0))
    }

    @Test
    fun `Should invoke operation again if scheduled again after initial execution`() = runBlocking {
        // Given that an operation has been scheduled to execute after the interval below
        val interval = 100L
        val scheduler = OperationScheduler(
            intervalMillis = interval,
            operation = observableOperation::doOperation
        )

        // When the interval has passed, and the the operation was rescheduled after several
        // subsequent intervals
        scheduler.maybeSchedule()
        delay(interval.withTimeForExecution)

        scheduler.maybeSchedule()
        delay(interval.withTimeForExecution)

        scheduler.maybeSchedule()
        delay(interval.withTimeForExecution)

        assertThat(observableOperation.executionCount, equalTo(3))
    }
}