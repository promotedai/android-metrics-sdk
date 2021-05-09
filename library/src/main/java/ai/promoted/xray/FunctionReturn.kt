package ai.promoted.xray

internal sealed class FunctionReturn<out T : Any> {
    data class Success<out T : Any>(val data: T) : FunctionReturn<T>()
    data class Failure(val throwable: Throwable) : FunctionReturn<Nothing>()
}
