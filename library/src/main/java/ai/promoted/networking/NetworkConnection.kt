package ai.promoted.networking

/**
 * Represents an abstract networking interface that can receive a [PromotedApiRequest] and execute
 * a network/HTTP request based off of the contents thereof.
 *
 * This allows users of the Promoted.Ai library to integrate the SDK into their own networking
 * stack, rather than being forced to include Promoted's chosen networking libraries
 * (like OkHttp/Retrofit).
 *
 * If an implementation of this interface is not provided by the user of the library via
 * configuration, then Promoted.Ai will provide its own implementation.
 */
interface NetworkConnection {
    suspend fun send(request: PromotedApiRequest)
}
