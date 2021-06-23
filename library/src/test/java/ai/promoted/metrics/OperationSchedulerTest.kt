package ai.promoted.metrics

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Ignore
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
    @Ignore("Flaky")
    fun `Should invoke operation only once after specified interval`() {
        // Given that an operation has been scheduled to execute after the interval below
        val interval = 100L
        val scheduler = OperationScheduler(
            intervalMillis = interval,
            operation = observableOperation::doOperation
        )
        scheduler.maybeSchedule()

        // When the interval has passed
        Thread.sleep(interval.withTimeForExecution)

        // Then the operation ran
        // and even as time goes on, the operation has only been invoked once
        assertThat("Execution count after 1 delay was ${observableOperation.executionCount}", observableOperation.executionCount, equalTo(1))
        Thread.sleep(interval.withTimeForExecution)
        Thread.sleep(interval.withTimeForExecution)
        assertThat("Execution count after 2 extra delays was ${observableOperation.executionCount}", observableOperation.executionCount, equalTo(1))
    }

    @Test
    fun `Should invoke operation only once when multiple schedules before interval`() {
        // Given that an operation has been scheduled to execute after the interval below
        val interval = 100L
        val scheduler = OperationScheduler(
            intervalMillis = interval,
            operation = observableOperation::doOperation
        )
        scheduler.maybeSchedule()

        // When a small amount of time has passed
        // and the operation was scheduled again
        // and then operation is scheduled after another small amount of time
        // and then finally the full interval has elapsed
        Thread.sleep(10L)
        scheduler.maybeSchedule()
        Thread.sleep(10L)
        scheduler.maybeSchedule()
        Thread.sleep((80L).withTimeForExecution)

        // Then the operation is still only invoked once
        assertThat("Execution count after multiple early schedules was ${observableOperation.executionCount}",  observableOperation.executionCount, equalTo(1))
    }

    @Test
    fun `Should not invoke operation at all when canceled before interval reached`() {
        // Given that an operation has been scheduled to execute after the interval below
        val interval = 100L
        val scheduler = OperationScheduler(
            intervalMillis = interval,
            operation = observableOperation::doOperation
        )
        scheduler.maybeSchedule()

        // When only half the interval has passed
        // and the operation was canceled
        Thread.sleep(interval / 2)
        scheduler.cancel()

        // Then the operation is never invoked, even after the full interval has passed
        // several times
        assertThat("Execution count after a half-delay was ${observableOperation.executionCount}",  observableOperation.executionCount, equalTo(0))

        Thread.sleep(interval.withTimeForExecution)
        Thread.sleep(interval.withTimeForExecution)

        assertThat("Execution count after 2 additional delays was ${observableOperation.executionCount}", observableOperation.executionCount, equalTo(0))
    }

    @Test
    fun `Should invoke operation again if scheduled again after initial execution`() {
        // Given that an operation has been scheduled to execute after the interval below
        val interval = 100L
        val scheduler = OperationScheduler(
            intervalMillis = interval,
            operation = observableOperation::doOperation
        )

        // When the interval has passed, and the the operation was rescheduled after several
        // subsequent intervals
        scheduler.maybeSchedule()
        Thread.sleep(interval.withTimeForExecution)

        scheduler.maybeSchedule()
        Thread.sleep(interval.withTimeForExecution)

        scheduler.maybeSchedule()
        Thread.sleep(interval.withTimeForExecution)

        assertThat("Execution count after 3 schedules/3 delays was ${observableOperation.executionCount}", observableOperation.executionCount, equalTo(3))
    }

    @Test
    fun `Should invoke second operation even after first one had been canceled`() {
        // Given that an operation has been scheduled to execute after the interval below
        // and then half-way through had been canceled
        val interval = 100L
        val scheduler = OperationScheduler(
            intervalMillis = interval,
            operation = observableOperation::doOperation
        )
        scheduler.maybeSchedule()
        Thread.sleep(interval / 2)
        scheduler.cancel()

        // When the operation is re-scheduled at a later point, after the original execution window
        // and interval for the *second* schedule elapses
        Thread.sleep(interval.withTimeForExecution)
        scheduler.maybeSchedule()
        Thread.sleep(interval.withTimeForExecution)

        // Then the operation is invoked only once
        assertThat(observableOperation.executionCount, equalTo(1))
    }
}