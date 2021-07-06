package ai.promoted

import ai.promoted.proto.event.ActionType
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * An activity representing a single Restaurant detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [RestaurantListActivity].
 */
class RestaurantDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_detail)
        setSupportActionBar(findViewById(R.id.detail_toolbar))

        val itemId = intent.getStringExtra(RestaurantDetailFragment.ARG_ITEM_ID)

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            // Notify Promoted that an action occurred
            PromotedAi.onAction("locate-item", ActionType.CUSTOM_ACTION_TYPE) {
                insertionId = itemId
            }
        }

        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don"t need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            val fragment = RestaurantDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(
                        RestaurantDetailFragment.ARG_ITEM_ID,
                        itemId
                    )
                }
            }

            supportFragmentManager.beginTransaction()
                .add(R.id.restaurant_detail_container, fragment)
                .commit()
        }
    }

    override fun onResume() {
        super.onResume()
        PromotedAi.onViewVisible("DetailActivity")
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            android.R.id.home -> {
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back

                navigateUpTo(Intent(this, RestaurantListActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}