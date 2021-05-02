package ai.promoted.internal

import android.content.res.Resources
import android.os.Build
import android.util.DisplayMetrics

/**
 * An abstraction to allow for DI/testability, so that static Android classes/methods (i.e.
 * Resources.getSystem() or Locale.getDefault()) need not be available to generate a Device
 * message when logging metrics.
 */
internal interface DeviceInfoProvider {
    val brand: String
    val manufacturer: String
    val model: String
    val sdkRelease: String
    val languageCode: String
    val countryCode: String
    val screenWidth: Int
    val screenHeight: Int
    val screenDensity: Float
}

/**
 * Default implementation of [DeviceInfoProvider] using Android system classes that provide the
 * necessary info. In the edge case that Android returns null for one of the required system-level
 * objects, defaults are used to keep things absolutely stable when providing device info.
 */
internal class AndroidDeviceInfoProvider : DeviceInfoProvider {
    private val systemLocale = SystemLocale.getDefault() ?: SystemLocale.ROOT
    private val displayMetrics = Resources.getSystem()?.displayMetrics ?: DisplayMetrics()

    override val brand: String
        get() = Build.BRAND
    override val manufacturer: String
        get() = Build.MANUFACTURER
    override val model: String
        get() = Build.MODEL
    override val sdkRelease: String
        get() = Build.VERSION.RELEASE
    override val languageCode: String
        get() = systemLocale.language
    override val countryCode: String
        get() = systemLocale.country
    override val screenWidth: Int
        get() = displayMetrics.widthPixels
    override val screenHeight: Int
        get() = displayMetrics.heightPixels
    override val screenDensity: Float
        get() = displayMetrics.density
}