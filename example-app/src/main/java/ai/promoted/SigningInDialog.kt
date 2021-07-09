package ai.promoted

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

class SigningInDialog : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.signing_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mock sign in - delay 3 sec then pretend we're auth'd
        Handler(Looper.getMainLooper()).postDelayed({

            // We're logged in, notify Promoted
            val userId = "User-${System.currentTimeMillis()}"
            PromotedAi.startSession(userId)

            startActivity(Intent(requireContext(), RestaurantListActivity::class.java))
            requireActivity().finish()
        }, 3000L)
    }
}