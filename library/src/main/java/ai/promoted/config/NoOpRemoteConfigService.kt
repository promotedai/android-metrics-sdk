package ai.promoted.config

/**
 * A class to be used in the case where there are no available [RemoteConfigService]s.
 */
// All functions here will be empty
@Suppress("EmptyFunctionBlock")
internal class NoOpRemoteConfigService : RemoteConfigService {
    override val latestRemoteConfig = RemoteConfig.empty()
    override fun fetchLatestConfigValues() {
    }
}
