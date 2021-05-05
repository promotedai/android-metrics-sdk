package ai.promoted;

import android.app.Application;

public class ExampleApplicationJavaDelegate {
    private static final String LOGGING_API_KEY = "OLpsrVSd565IQmOAR62dO9GkXUJngNo5ZUdCMV70";
    private static final String LOGGING_URL =
            "https://5tbepnh11h.execute-api.us-east-2.amazonaws.com/dev/main";

    public void onCreate(Application application) {
        PromotedAi
                .buildConfiguration()
                .withMetricsLoggingUrl(LOGGING_URL)
                .withMetricsLoggingApiKey(LOGGING_API_KEY)
                .initialize(application);
    }
}
