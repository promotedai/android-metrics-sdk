package ai.promoted

import ai.promoted.proto.event.ActionType
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
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

        rv.adapter = Adapter(fakeContent)

        PromotedAi.trackRecyclerView(
            recyclerView = rv,
            contentProvider = object : RecyclerViewTracking.ContentProvider {
                override fun provideLatestData(): List<AbstractContent> {
                    return fakeContent
                }
            }
        ) {
            percentageThreshold = 50.0
//            timeThresholdMillis = 3000L
        }
    }

    private class Adapter(private val content: List<AbstractContent>) :
        RecyclerView.Adapter<Adapter.VH>() {
        private class VH(itemView: View) : RecyclerView.ViewHolder(itemView)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val view =
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.fake_content, parent, false)

            return VH(view)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            val content = content[position]
            holder.itemView.findViewById<TextView>(R.id.name).text = content.name
            holder.itemView.findViewById<Button>(R.id.open).setOnClickListener {
                PromotedAi.onAction("click-${content.name}", ActionType.CUSTOM_ACTION_TYPE) {
                    insertionId = content.insertionId
                }
            }
        }

        override fun getItemCount(): Int {
            return content.size
        }
    }
}