package ai.promoted.config

internal interface RemoteConfigService {
    val latestRemoteConfig: RemoteConfig
    fun fetchLatestConfigValues()
}
