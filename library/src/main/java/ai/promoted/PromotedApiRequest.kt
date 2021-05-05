package ai.promoted

/**
 * An abstract representation of data that is to be POSTed to a Promoted.Ai API
 */
class PromotedApiRequest(
    val url: String,
    val headers: Map<String, String>,
    val bodyData: ByteArray
)
