package ai.promoted.java;

import android.app.Application;

import ai.promoted.PromotedAi;
import ai.promoted.metrics.usecases.anomaly.ModalAnomalyActivity;

public class ExampleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        PromotedAi
                .buildConfiguration()
                .withMetricsLoggingApiKey("")
                .withMetricsLoggingUrl("")
                .initialize(this);
    }
}
