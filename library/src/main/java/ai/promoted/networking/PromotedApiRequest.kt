package ai.promoted.networking

class PromotedApiRequest(
    val url: String,
    val headers: Map<String, String>,
    val bodyData: ByteArray
)