package ai.promoted;

import android.app.Activity;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ai.promoted.proto.event.ActionType;

public class MainActivityJavaDelegate {
    public void onCreate(Activity activity) {
        String userId = "User-" + System.currentTimeMillis();
        setupRecyclerView(activity);
        PromotedAi.startSession(userId);
    }

    public void onResume() {
        PromotedAi.onViewVisible("MainActivityJava");
    }

    private void setupRecyclerView(Activity activity) {
        RecyclerView rv = activity.findViewById(R.id.rv);
        ArrayList<AbstractContent> fakeContent = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            fakeContent.add(
                    new AbstractContent.Item(
                            "Item " + i,
                            "content-id-" + i,
                            null
                    )
            );
        }

        Adapter adapter = new Adapter(fakeContent, item -> {
            PromotedAi
                    .buildAction()
                    .withName("click-item-" + item.getName())
                    .withType(ActionType.CUSTOM_ACTION_TYPE)
                    .withContentId("content-id-click-item-"+item.getName())
                    .log();
        });

        rv.setAdapter(adapter);

        PromotedAi
                .buildRecyclerViewTracking()
                .withPercentageThreshold(50.0)
                .startTracking(rv, () -> fakeContent);
    }
}
