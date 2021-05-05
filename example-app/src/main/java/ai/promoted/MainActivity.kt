package ai.promoted

import ai.promoted.proto.event.ActionType
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val javaDelegate = MainActivityJavaDelegate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.do_action_btn)

        if(USE_JAVA) return javaDelegate.onCreate(button)

        val userId = "User-${System.currentTimeMillis()}"

        PromotedAi.startSession(userId)

        button.setOnClickListener {
            PromotedAi.onAction("do-action-btn", ActionType.CUSTOM_ACTION_TYPE) {
                insertionId = "insertion-id"
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if(USE_JAVA) return javaDelegate.onResume()

        PromotedAi.onViewVisible("MainActivity")
    }
}