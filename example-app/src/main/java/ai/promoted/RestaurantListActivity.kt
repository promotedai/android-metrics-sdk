package ai.promoted

import ai.promoted.dummy.DummyContent
import ai.promoted.proto.event.ActionType
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [RestaurantDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class RestaurantListActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_list)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title

        if (findViewById<NestedScrollView>(R.id.restaurant_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        setupRecyclerView(findViewById(R.id.restaurant_list))
    }

    override fun onResume() {
        super.onResume()
        PromotedAi.onViewVisible("ListActivity")
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, twoPane)

        PromotedAi.trackRecyclerView(
            recyclerView = recyclerView,
            currentDataProvider = {
                DummyContent.ITEMS.map {
                    AbstractContent.Content(
                        name = it.id,
                        contentId = it.id
                    )
                }
            }
        ) {
            percentageThreshold = 50.0
            // Uncomment to give a time threshold for impressions
//            timeThresholdMillis = 3000L
        }
    }

    class SimpleItemRecyclerViewAdapter(
        private val parentActivity: RestaurantListActivity,
        private val values: List<DummyContent.DummyItem>,
        private val twoPane: Boolean
    ) :
        RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener = View.OnClickListener { v ->
            val item = v.tag as DummyContent.DummyItem
            if (twoPane) {
                val fragment = RestaurantDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(RestaurantDetailFragment.ARG_ITEM_ID, item.id)
                    }
                }
                parentActivity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.restaurant_detail_container, fragment)
                    .commit()
            } else {
                val intent = Intent(v.context, RestaurantDetailActivity::class.java).apply {
                    putExtra(RestaurantDetailFragment.ARG_ITEM_ID, item.id)
                }
                v.context.startActivity(intent)
            }

            // Notify Promoted that an action occurred
            PromotedAi.onAction("open-restaurant", ActionType.CUSTOM_ACTION_TYPE) {
                insertionId = item.id
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.restaurant_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.idView.text = item.id

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val idView: TextView = view.findViewById(R.id.id_text)
        }
    }
}