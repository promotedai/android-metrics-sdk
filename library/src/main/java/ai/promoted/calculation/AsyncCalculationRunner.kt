package ai.promoted.calculation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/**
 * Allows you to define what thread/[CoroutineContext] you want an operation to be executed upon,
 * and then guarantee sequential execution of that operation on subsequent calls.
 */
internal class AsyncCalculationRunner(
    private val calculationContext: CoroutineContext = Dispatchers.Default,
    private val notificationContext: CoroutineContext = calculationContext
) {
    private val scope = CoroutineScope(calculationContext)
    private val mutex = Mutex()

    fun <Input, Output> scheduleCalculation(
        input: Input,
        calculation: (input: Input) -> Output,
        onResult: (output: Output) -> Unit
    ) {
        scope.launch {
            mutex.withLock {
                val output = calculation.invoke(input)
                withContext(notificationContext) {
                    onResult.invoke(output)
                }
            }
        }
    }
}
