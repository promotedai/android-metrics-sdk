package ai.promoted.platform

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Build
import android.os.Debug
import java.io.ByteArrayInputStream
import java.lang.ref.WeakReference
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

interface AppRuntimeEnvironment {
    val isNonProdBuild: Boolean
    val isDebuggerConnected: Boolean
    val isRunningOnEmulator: Boolean

    fun initialize(context: Context)

    fun dumpRuntimeInfo(): String {
        return """
            isNonProdBuild: $isNonProdBuild
            isDebuggerConnected: $isDebuggerConnected
            isRunningOnEmulator: $isRunningOnEmulator
        """.trimIndent()
    }

    companion object {
        val default: AppRuntimeEnvironment by lazy { Default }
    }
}

private object Default : AppRuntimeEnvironment {
    private var contextRef: WeakReference<Context?>? = WeakReference(null)
    private val context: Context? get() = contextRef?.get()

    override val isNonProdBuild: Boolean
        get() = isAppDebuggable() ?: getBuildConfigDebug() ?: hasDebugCert() ?: false

    override val isDebuggerConnected: Boolean
        get() = Debug.isDebuggerConnected()
                || Debug.waitingForDebugger()

    override val isRunningOnEmulator: Boolean
        get() = Build.FINGERPRINT.startsWith("google/sdk_gphone")

    override fun initialize(context: Context) {
        contextRef = WeakReference(context)
    }

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    private fun isAppDebuggable(): Boolean? =
        try {
            context!!.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        } catch (throwable: Throwable) {
            null
        }

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    private fun getBuildConfigDebug(): Boolean? =
        try {
            // Try to infer the client application's fully qualified class name for BuildConfig.
            val buildConfigClass = Class.forName(context!!.packageName.toString() + ".BuildConfig")

            // Get the "BuildConfig.DEBUG" field.
            val field = buildConfigClass.getField("DEBUG")

            field.get(null) as Boolean
        } catch (throwable: Throwable) {
            null
        }

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    @SuppressLint("PackageManagerGetSignatures")
    private fun hasDebugCert(): Boolean? =
        try {
            val pinfo: PackageInfo =
                context!!.packageManager.getPackageInfo(
                    context!!.packageName,
                    PackageManager.GET_SIGNATURES
                )

            signaturesContainDebugSignature(pinfo.signatures)
        } catch (throwable: Throwable) {
            null
        }

    private fun signaturesContainDebugSignature(signatures: Array<Signature>): Boolean {
        val cf = CertificateFactory.getInstance("X.509")
        for (i in signatures.indices) {
            val stream = ByteArrayInputStream(signatures[i].toByteArray())
            val cert = cf.generateCertificate(stream) as X509Certificate

            if (cert
                    .issuerX500Principal
                    .name
                    .contains("CN=Android")
                && cert
                    .issuerX500Principal
                    .name
                    .contains("O=Android")
            ) {
                return true
            }
        }

        return false
    }
}
