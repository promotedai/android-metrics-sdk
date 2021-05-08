package ai.promoted

import ai.promoted.proto.event.ActionType
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private val javaDelegate = MainActivityJavaDelegate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (USE_JAVA) return javaDelegate.onCreate(this)

        setupRecyclerView()

        val userId = "User-${System.currentTimeMillis()}"
        PromotedAi.startSession(userId)
    }

    override fun onResume() {
        super.onResume()

        if (USE_JAVA) return javaDelegate.onResume()

        PromotedAi.onViewVisible("MainActivity")
    }

    private fun setupRecyclerView() {
        val rv = findViewById<RecyclerView>(R.id.rv)
        val fakeContent = mutableListOf<AbstractContent.Item>()

        repeat(100) {
            fakeContent.add(
                AbstractContent.Item(
                    name = "Item $it",
                    insertionId = "insertion-id-$it"
                )
            )
        }

        rv.adapter = Adapter(fakeContent) {
            PromotedAi.onAction("click-item-${it.name}", ActionType.CUSTOM_ACTION_TYPE) {
                insertionId = "click-item-insertion-${it.name}"
            }
        }

        PromotedAi.trackRecyclerView(
            recyclerView = rv,
            currentDataProvider = { fakeContent }
        ) {
            percentageThreshold = 50.0
//            timeThresholdMillis = 3000L
        }
    }
}