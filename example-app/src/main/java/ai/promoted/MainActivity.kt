package ai.promoted

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userId = "User-${System.currentTimeMillis()}"
        PromotedAi.startSession(userId)
    }

    override fun onResume() {
        super.onResume()
        // PromotedAi.trackView
        // PromotedAi.trackActivityLifecycle(this)
        // PromtoedAi.trackFragmentLifecycle(this)
        // PromtoedAi.onActivityResume(thsi)

    }
}