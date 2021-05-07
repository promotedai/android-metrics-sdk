package ai.promoted.ui.recyclerview

/**
 * A singular representation of a row being visible within a RecyclerView.
 */
data class RowVisibility(
    val position: Int,
    val visibleSinceMillis: Long,
    val percentageVisible: Double
)
