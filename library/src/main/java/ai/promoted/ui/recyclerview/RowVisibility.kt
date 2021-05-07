package ai.promoted.ui.recyclerview

data class RowVisibility(
    val position: Int,
    val visibleSinceMillis: Long,
    val percentageVisible: Double
)
