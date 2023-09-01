package ai.promoted.metrics.id

/**
 * Represents any of the top-level "ancestor" IDs (i.e. session ID, view ID, anonUserID) which
 * can either be explicitly set by the library user, or implicitly set by calling [advance].
 */
internal class AncestorId(
    idGenerator: IdGenerator
) {
    private val advanceableId = AdvanceableId(
        skipFirstAdvancement = true,
        idGenerator
    )

    private val pendingImplicitValue = advanceableId.currentValue

    /**
     * Whether a call to [override] has been made. This is useful if you want to modify or ignore
     * behavior based on whether the ancestor ID was overridden by a library user.
     */
    var isOverridden = false
        private set

    /**
     * The current value of the ID, whether it was set by [override] or [advance]. Will return an
     * empty string if neither [advance] nor [override] have been called.
     */
    var currentValue = ""
        private set

    /**
     * The current value of the ID, or (if no current value exists), the value that will be applied
     * to [currentValue] after the first call to [advance]
     */
    val currentOrPendingValue
        get() = if (currentValue.isEmpty()) pendingImplicitValue
        else currentValue

    /**
     * The current value of the ID, or null if it has not yet been set by [advance] or [override].
     */
    val currentValueOrNull
        get() = if (currentValue.isEmpty()) null
        else currentValue

    /**
     * Move to the next implicit / internally-generated ID value
     */
    fun advance() {
        advanceableId.advance()
        this.currentValue = advanceableId.currentValue
    }

    /**
     * Manually set the ID's value until the next advance() call
     */
    fun override(explicitValue: String) {
        this.currentValue = explicitValue
        this.isOverridden = true
    }
}
