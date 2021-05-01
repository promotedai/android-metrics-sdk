package ai.promoted.internal

/**
 * Represents the returned result from a suspendable function
 */
internal sealed class Return<out T : Any>

/**
 * Short-hand for a [Return] of type [Unit]
 */
internal typealias Completable = Return<Unit>

/**
 * A successful [Return] with the returned data, of type T
 */
internal data class Success<out T : Any>(val data: T) : Return<T>()

/**
 * A successful [Return] with no associated data
 */
@Suppress("FunctionName")
internal fun Success(): Success<Unit> = Success(Unit)

/**
 * A failure [Return] with an associated [Throwable]
 */
internal data class Failure(val throwable: Throwable) : Return<Nothing>()

/**
 * Extension function allowing for chained definition of logic on a [Return].
 *
 * Example:
 *  theSuspendingFunction()
 *      .onSuccess {
 *          println("on success")
 *      }
 *      .onError { throwable ->
 *          throwable.printStackTrace()
 *      }
 */
internal inline fun <T : Any> Return<T>.onSuccess(action: (T) -> Unit): Return<T> {
    if (this is Success) action(data)
    return this
}

/**
 * Same use as [onSuccess], but for handling a return of type [Failure]
 */
internal inline fun <T : Any> Return<T>.onFailure(action: (Failure) -> Unit): Return<T> {
    if (this is Failure) action(this)
    return this
}

internal fun <T : Any> Return<T>.unwrapOrThrow(): T = when (this) {
    is Success -> data
    is Failure -> throw throwable
}

internal fun <T : Any> Return<T>.unwrapOrDefault(defaultValue: T): T = when (this) {
    is Success -> data
    is Failure -> {
        throwable.printStackTrace()
        defaultValue
    }
}

internal fun <T : Any> Return<T>.unwrapOrNull(): T? = when (this) {
    is Success -> data
    is Failure -> {
        throwable.printStackTrace()
        null
    }
}

internal fun Return<Unit>.unwrapOrIgnore() =
    when (this) {
        is Success -> {
        }
        is Failure -> throwable.printStackTrace()
    }