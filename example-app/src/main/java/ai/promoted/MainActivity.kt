package ai.promoted

import ai.promoted.proto.event.ActionType
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userId = "User-${System.currentTimeMillis()}"

        PromotedAi.startSession(userId)

        findViewById<Button>(R.id.do_action_btn).setOnClickListener {
            PromotedAi.onAction("do-action-btn", ActionType.CUSTOM_ACTION_TYPE) {
                insertionId = "insertion-id"
            }
        }
    }

    override fun onResume() {
        super.onResume()
        PromotedAi.onViewVisible("MainActivity")
    }
}