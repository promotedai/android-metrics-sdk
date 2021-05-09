package ai.promoted.xray

import ai.promoted.platform.Clock
import java.util.regex.Pattern
import kotlin.reflect.KClass

internal class FunctionMonitor(
    private val clock: Clock
) {
    private val anonymousClassPattern = Pattern.compile("(\\$\\d+)+$")

    @Suppress("TooGenericExceptionCaught")
    fun <T : Any> monitored(
        stackElementsToExclude: List<KClass<*>> = emptyList(),
        block: () -> T
    ): MonitorResult<T> {
        val blockTag = generateMonitorTag(stackElementsToExclude)
        val start = clock.currentTimeMillis

        val functionReturn = try {
            FunctionReturn.Success(block.invoke())
        } catch (error: Throwable) {
            FunctionReturn.Failure(error)
        }

        return MonitorResult(
            tag = blockTag,
            elapsedTimeMillis = start.elapsedTimeUntilNow,
            functionReturn = functionReturn
        )
    }

    @Suppress("TooGenericExceptionCaught")
    suspend fun <T : Any> monitoredSuspend(
        stackElementsToExclude: List<KClass<*>> = emptyList(),
        block: suspend () -> T
    ): MonitorResult<T> {
        val blockTag = generateMonitorTag(stackElementsToExclude)
        val start = clock.currentTimeMillis

        val functionReturn = try {
            FunctionReturn.Success(block.invoke())
        } catch (error: Throwable) {
            FunctionReturn.Failure(error)
        }

        return MonitorResult(
            tag = blockTag,
            elapsedTimeMillis = start.elapsedTimeUntilNow,
            functionReturn = functionReturn
        )
    }

    /**
     * When retrieved, will return a string that follows the pattern "ClassName.function", based
     * on the calling site of the log statement.
     */
    private fun generateMonitorTag(stackElementsToExclude: List<KClass<*>>): String =
        Throwable().stackTrace
            // Excludes this class, and any additional classes specified by the caller, from the
            // stacktrace (so as to be most clear about the original call site of the function
            // being monitored)
            .first { element ->
                element.className != FunctionMonitor::class.java.name
                        && !stackElementsToExclude.any { classToExclude ->
                    element.className == classToExclude.java.name
                }
            }
            .let(::createStackElementTag)

    /**
     * Extract the tag which should be used for the message from the `element`. By default
     * this will use the class name without any anonymous class suffixes (e.g., `Foo$1`
     * becomes `Foo`).
     */
    private fun createStackElementTag(element: StackTraceElement): String {
        var tag = element.className.substringAfterLast('.') + "." + element.methodName
        val m = anonymousClassPattern.matcher(tag)
        if (m.find()) {
            tag = m.replaceAll("")
        }
        return tag
    }

    private val Long.elapsedTimeUntilNow
        get() = clock.currentTimeMillis - this
}
