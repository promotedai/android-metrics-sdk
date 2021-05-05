package ai.promoted;

import android.view.View;

import ai.promoted.proto.event.ActionType;

public class MainActivityJavaDelegate {
    public void onCreate(View actionButton) {
        String userId = "User-" + System.currentTimeMillis();
        PromotedAi.startSession(userId);
        actionButton.setOnClickListener(v -> PromotedAi
                .buildAction()
                .withName("java-action-button")
                .withType(ActionType.CUSTOM_ACTION_TYPE)
                .log());
    }

    public void onResume() {
        PromotedAi.onViewVisible("MainActivityJava");
    }
}
