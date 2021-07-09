package ai.promoted.java;

import android.app.Activity;

import ai.promoted.PromotedAi;

public class ExampleActivity extends Activity {
    @Override
    protected void onResume() {
        super.onResume();
        PromotedAi.onViewVisible("ExampleActivity");
    }
}
