package ai.promoted.ui.recyclerview

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
    private val visibilityThreshold: VisibilityThreshold,
    private val currentDataProvider: () -> List<RowData>,
    private val onVisibleRowsChanged: (rows: List<RowData>) -> Unit,
    private val onRecyclerViewDetached: () -> Unit
) {
    private var scrollListener: RecyclerView.OnScrollListener? = null

    init {
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is LinearLayoutManager) trackRecyclerView(recyclerView, layoutManager)
    }

    fun stopTracking() {
        scrollListener?.let { scrollListener ->
            recyclerView.post { recyclerView.removeOnScrollListener(scrollListener) }
        }
    }

    private fun trackRecyclerView(recyclerView: RecyclerView, layoutManager: LinearLayoutManager) {
        val visibleDataCalculator = AsyncVisibleDataCalculator(
            clock = clock,
            recyclerView = recyclerView,
            layoutManager = layoutManager,
            threshold = visibilityThreshold,
            currentDataProvider = currentDataProvider,
            onVisibleRowsChanged = onVisibleRowsChanged
        )

        val scrollListener = createScrollListener(visibleDataCalculator)
        postListeners(scrollListener, visibleDataCalculator)
    }

    private fun postListeners(
        scrollListener: RecyclerView.OnScrollListener,
        visibleDataCalculator: AsyncVisibleDataCalculator<RowData>
    ) {
        recyclerView.post {
            recyclerView.addOnScrollListener(scrollListener)
            this.scrollListener = scrollListener

            recyclerView.doOnLayout { visibleDataCalculator.calculateVisibleData() }
            recyclerView.doOnDetach {
                recyclerView.removeOnScrollListener(scrollListener)
                this.scrollListener = null
                onRecyclerViewDetached.invoke()
            }
        }
    }

    private fun createScrollListener(visibleDataCalculator: AsyncVisibleDataCalculator<RowData>) =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                visibleDataCalculator.calculateVisibleData()
            }
        }
}
