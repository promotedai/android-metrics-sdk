package ai.promoted.ui.recyclerview

import ai.promoted.RecyclerViewTracking
import ai.promoted.platform.Clock
import androidx.core.view.doOnDetach
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Given a [RecyclerView], this class allows you to be notified when the list of currently visible
 * rows changes. This would be when the view is first rendered, when the user scrolls, or when the
 * view disappears.
 */
internal class Tracker<RowData : Any>(
    private val clock: Clock,
    private val recyclerView: RecyclerView,
    private val threshold: RecyclerViewTracking.VisibilityThreshold,
    private val latestDataProvider: () -> List<RowData>,
    private val onVisibleRowsChanged: (rows: List<RowData>) -> Unit
) {
    private var scrollListener: RecyclerView.OnScrollListener? = null

    init {
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is LinearLayoutManager) trackRecyclerView(recyclerView, layoutManager)
    }

    fun stopTracking() {
        scrollListener?.let { recyclerView.removeOnScrollListener(it) }
    }

    private fun trackRecyclerView(recyclerView: RecyclerView, layoutManager: LinearLayoutManager) {
        val visibleDataCalculator = VisibleDataCalculator(
            clock = clock,
            recyclerView = recyclerView,
            layoutManager = layoutManager,
            threshold = threshold,
            latestDataProvider = latestDataProvider,
            onVisibleRowsChanged = onVisibleRowsChanged
        )

        val scrollListener = createScrollListener(visibleDataCalculator)
        recyclerView.addOnScrollListener(scrollListener)

        recyclerView.doOnLayout { visibleDataCalculator.calculateVisibleData() }
        recyclerView.doOnDetach {
            recyclerView.removeOnScrollListener(scrollListener)
            onVisibleRowsChanged.invoke(emptyList())
        }

        this.scrollListener = scrollListener
    }

    private fun createScrollListener(visibleDataCalculator: VisibleDataCalculator<RowData>) =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                visibleDataCalculator.calculateVisibleData()
            }
        }
}
