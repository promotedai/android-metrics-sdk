package ai.promoted.metrics

/**
 * Represents a unit of trackable content in your marketplace. For better conceptualization in your
 * code, prefer to use one of the subclasses (such as `Item` or `Partner`) instead of `Content` if
 * they apply.
 */
sealed class AbstractContent {
    abstract val name: String
    abstract val contentId: String?
    abstract val insertionId: String?

    data class Content(
        override val name: String,
        override val contentId: String? = null,
        override val insertionId: String? = null
    ) : AbstractContent()

    data class Item(
        override val name: String,
        override val contentId: String? = null,
        override val insertionId: String? = null
    ) : AbstractContent()

    data class Partner(
        override val name: String,
        override val contentId: String? = null,
        override val insertionId: String? = null
    ) : AbstractContent()
}
