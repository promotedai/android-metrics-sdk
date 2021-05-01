package ai.promoted.networking

interface NetworkConnection {
    suspend fun send(request: PromotedApiRequest)
}