package ai.promoted;

import android.app.Activity;

public class MainActivityJavaDelegate {
    public void onCreate(Activity activity) {
        String userId = "User-" + System.currentTimeMillis();
        PromotedAi.startSession(userId);
    }

    public void onResume() {
        PromotedAi.onViewVisible("MainActivityJava");
    }
}
