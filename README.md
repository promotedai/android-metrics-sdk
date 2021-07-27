# Promoted.ai Android Client Library
[![Main](https://github.com/promotedai/android-metrics-sdk/actions/workflows/main.yml/badge.svg)](https://github.com/promotedai/android-metrics-sdk/actions/workflows/main.yml)
![INSTRUCTION](https://img.shields.io/badge/instruction--coverage-71%25-yellow.svg)

Promoted.ai provides an Android logging library for integration with Android apps. This library contains the functionality required to track events in your Android app and deliver them efficiently to Promoted.ai backends.

Our client library is available for Android 7.0+ (API 24+), and works with both Kotlin and Java apps.

## Technology
Our client library is built on a number of proven technologies:

1. Protocol Buffers: A data exchange format that is much more efficient over the wire than JSON, making our data usage much lower than JSON-based logging solutions.
1. OkHttp/Retrofit: An industry-standard library for network access
1. Firebase Remote Config (roadmap): Provides server-side configuration of the client library’s behavior without the need for an additional Play Store review/release cycle.
1. gRPC (roadmap): A high-performance RPC framework used by many Google apps.

## Availability
Our client library is available via the following channels. You will receive instructions on how to integrate via the channel you choose.

1. As a Maven-published dependency using Gradle
1. As an NPM package (React Native only). See [react-native-metrics](https://github.com/promotedai/react-native-metrics).

## Integration
The library is designed to have a single entry-point for integration. All SDK functions may be executed through the statically available `PromotedAi` class. The `PromotedAi` class is also designed to support fluent usage both from Java and Kotlin to enable maximum succinctness and readability.

*Note: While the API is accessed via static functions, library users can rest assured that all SDK objects are, internally, properly scoped for memory management. The public static interface is purely for ease of use.*

### Initialization
Initialization should occur as soon as possible when your app starts (i.e. `Application.onCreate()`). The SDK will be non-operational until initialization occurs. *Note: the SDK will **not** crash if used prior to initialization, it will simply ignore calls when in the un-initialized state*.

There are two primary ways to initialize the SDK, one targeted toward the Kotlin user and one to the Java user. Examples are below:

#### Kotlin
```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // "this" = MyApplication
        // The { ... } is a configuration block that allows you to declaratively dictate 
        // configuration options for how Promoted.Ai should run. The SDK will still run
        // even if no configuration options are provided; however, no logs will be sent
        // without at least a logging URL and API key.
        PromotedAi.initialize(this) {
            metricsLoggingUrl = "<the URL for your Promoted logging endpoint>"
            metricsLoggingApiKey = "<your Promoted API key>"
        }
    }
}
```

#### Java
```java
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        
        // "this" = MyApplication
        // The buildConfiguration() function returns an SdkBuilder, which allows you
        // to declaratively dictate options for how Promoted.Ai should run.
        // You must end your chained call with initialize(...) in order to complete
        // the SDK initialization.
        PromotedAi
            .buildConfiguration()
            .withMetricsLoggingUrl("<the URL for your Promoted logging endpoint>")
            .withMetricsLoggingApiKey("<your Promoted API key>")
            .initialize(this);
    }
}
```

### Logging
The primary function of the SDK is to enable logging / tracking of various events in a given session with the application. Event types that are currently trackable are:
- Session start (a user has started a session)
- View open (a new view is now displaying to the user)
- Impressions (single impressions or impressions from a scrolling list)
- Actions (domain-specific, user-initiated events like "add to cart" or "checkout")

Each of these is described in detail below.

#### Tracking sessions
You may track the start of the app session and provide the user ID of the current user. If the user does not have an account/ID set up yet, you may still track the session by just omitting the user ID. Examples below are applicable to both Kotlin and Java:
```kotlin
// Session start with a user ID
PromotedAi.startSession("a_user@yourdomain.com")

// Session start for a signed out user
PromotedAi.startSession()
```

#### Tracking views
You may track a new view (i.e. `Activity` or `Fragment`) becoming visible to the user by calling `onViewVisible` with a string that acts as a label for this view. Examples below are applicable to both Kotlin and Java:
```kotlin
PromotedAi.onViewVisible("settings-screen")
```

#### Tracking single impressions
You may track a single impression for a piece of content that was displayed to the user. You should provide at least one identifier via the `ImpressionData` block / builder. Examples for both Kotlin and Java below.
```kotlin
PromotedAi.onImpression {
    insertionId = "<insertion ID>"
    contentId = "<content ID>"
}
```

```java
// Note that log() must be called to submit the impression
PromotedAi
    .buildImpression()
    .withInsertionId("<insertion ID>")
    .withContentId("<content ID>")
    .log();
```

#### Tracking collections (multiple impressions occurring from scrolling through a list)
The SDK supports tracking impressions based on collections of content (rather than just the singular impression tracking via `onImpression()`). There are two primary ways to track collection-based impressions: either by directly notifying the SDK when a new collection of impressions has occurred, or by using the automatic scroll tracking provided for `RecyclerView`s. In either case, your app's content must be provided to the SDK in the form of an `AbstractContent` object (this is basically an object holding content identifiers in order to track impressions per each piece of content). 

The SDK also supports tracking several collections at once (i.e. if you have two different scrolling lists within the same top-level view). For manual tracking, you will have to provide a string key so that the SDK can distinguish between different collections that are being tracked. For automatic tracking, no string key is needed, as it will be automatically generated based on the `RecyclerView`s view ID.

##### Manually tracking collections
You may be displaying your collections in a custom scrolling view, or in a more advanced `RecyclerView` (i.e. a staggered grid). In this case, you will need to manually track when content becomes visible as the user scrolls through your collection and then notify the SDK when the list of currently-visible content has changed. Examples below are applicable to both Java and Kotlin:

```kotlin
// Call this when a new list of content has been displayed to the user
// This should only represent the currently *visible* content, not the
// entire list of content available to scroll
PromotedAi.onCollectionVisible(
    "<a key/label for this specific collection view>", 
    listOfCurrentlyVisibleContent
);

// Call this when the user has scrolled, or the set of content visible
// to the user has otherwise changed. Note: this should not be a
// differential (the SDK takes care of diff'ing); it should represent 
// the entire set of content that is currently within the view-port
PromotedAi.onCollectionUpdated(
    "<a key/label for this specific collection view>", 
    listOfCurrentlyVisibleContent
);
```

Upon a call to `PromotedAi.onCollectionUpdated()`, the SDK will calculate the difference between the last call and then log any new content impressions. All differential calculation is done off of the main thread in order to ensure calls to the SDK that occur upon user-scroll events do not hamper scrolling performance.

When your collection view has been dismissed or hidden, please call `onCollectionHidden` so that any resources related to collection tracking may be cleaned up. Example below applies to both Kotlin and Java:
```kotlin
PromotedAi.onCollectionHidden("<a key/label for this specific collection view>")
```

##### Automatically tracking collections via RecyclerViews
If you are using a `RecyclerView` with a `LinearLayoutManager` (typical, single-column, scrolling collection of content), then you may use the SDK's automatic `RecyclerView` tracking. When the user scrolls the `RecyclerView`, the SDK will automatically calculate which content is currently visible and subsequently log which impressions have occurred. *Note: all visibility calculation and content differential calculation is executed off of the main thread in order to preserve the scrolling performance of the `RecyclerView`).*

###### Providing the data that is backing a `RecyclerView`
In order to automatically track impressions / impression IDs for each piece of your content, you must provide a function-reference (Kotlin) or interface implementation (Java) for a "current data provider" that may be invoked by the SDK at any time so that the SDK can determine what the current baseline of content is. In other words, the SDK must be able to access your `RecyclerView.Adapter`'s backing data in order to accurately map from what views are visible to what *data* is currently being displayed and which impressions have occurred. An example of this will be provided below.

###### Impression thresholds for fine-tuned impression tracking
When using the `RecyclerView` impression tracking, you may define custom impression thresholds that will ensure impression logging matches your business needs for what a "true" impression is. For example, a user may fling a list of content and thus scroll by many pieces of content that may be tracked as impressions; but, in reality, the user was not actually able to *see* the content, thus a true impression did not occur. To mitigate this, you may define two primary thresholds, percentage visible and time visible, that will ensure only true impressions are logged when a user is scrolling through your `RecyclerView`. An example of this is provided below.

###### RecyclerView tracking examples
Below are examples of automatically tracking a `RecyclerView`. Note that defining the impression thresholds looks uses the same block/builder pattern used elsewhere throughout the SDK.

**Kotlin**
```kotlin
private fun getCurrentContent(): List<AbstractContent> {
    return myAdapter.currentContent
}

...

PromotedAi.trackRecyclerView(
    yourRecyclerView,
    this::getCurrentContent
) {
    // Minimum amount of visibility required for a true impression
    // to have occurred
    percentageThreshold = 50.0
    
    // Minimum amount of time visible required for a true
    // impression to have occurred
    timeThresholdMillis = 1000L
}
```

**Java**
```java
private CurrentDataProvider rvDataProvider = new CurrentDataProvider() {
    @Override
    public List<AbstractContent> provideCurrentData() { 
        return myAdapter.currentContent
    }
}

...

// Note that you must finalize the chain with startTracking(), where you pass
// in your RecyclerView and your CurrentDataProvider object
PromotedAi
    .buildRecyclerViewTracking()
    .withPercentageThreshold(50.0)
    .withTimeThreshold(1000L)
    .startTracking(
        yourRecyclerView,
        rvDataProvider
    );
```

#### Tracking actions
You may track user-initiated events/actions using `PromotedAi.onAction()` (Kotlin) and `PromotedAi.buildAction()` (Java). The SDK provides several common action types, along with a "custom" action type for actions that are not covered by the pre-defined `ActionType` enum values. Similar to impressions, the SDK allows you to provide identifiers that may be associated to the impression that occurred when the user took this action. These identifiers are not required for actions, as a unique action ID will be generated even if no impression-related IDs are provided. 

Examples in Kotlin and Java are below:
##### Kotlin
```kotlin
PromotedAi.onAction("add-to-cart", ActionType.ADD_TO_CART) {
    insertionId = "<insertion ID>"
    contentId = "<content ID>"
}
```
##### Java
```java
// Note that the chain must be finalized with a call to log() in order to be submitted
PromotedAi
    .buildAction()
    .withName("add-to-cart")
    .withType(ActionType.ADD_TO_CART)
    .withInsertionId("<insertion ID>")
    .withContentId("<content ID>")
    .log();
```

### X-ray
The SDK allows you to track SDK performance via LogCat messages. This is what is referred to as **X-ray**. This feature is optional and disabled by default. You may enable it simply as part of your initialization/configuration block when you initialize the SDK. Once enabled, X-ray will log messages to LogCat that report how long various functions of the SDK are taking to execute (including network calls). Examples of enabling X-ray (in both Kotlin and Java) are below.
#### Kotlin
```kotlin
PromotedAi.initialize(yourApplicationObject) {
    /* standard configuration */
    ...

    // Enable X-ray
    xrayEnabled = true
}
```

#### Java
```java
PromotedAi
    .buildConfiguration()
    /* standard configuration */
    ...
    .withXrayEnabled(true)
    .initialize(yourApplication)
```
