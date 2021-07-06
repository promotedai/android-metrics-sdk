package ai.promoted

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.sign_in_button).setOnClickListener {
            SigningInDialog().show(supportFragmentManager, "signing-in")
        }
    }
}