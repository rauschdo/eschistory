package de.rauschdo.eschistory.utility.browser

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.text.TextUtils
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsSession
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import timber.log.Timber

const val STABLE_PACKAGE = "com.android.chrome"
const val BETA_PACKAGE = "com.chrome.beta"
const val DEV_PACKAGE = "com.chrome.dev"
const val LOCAL_PACKAGE = "com.google.android.apps.chrome"

/**
 * Helper class for Custom Tabs.
 */
object CustomTabsHelper {

    private const val TAG = "CustomTabsHelper"

    val unsupportedList: List<String>
        get() = listOf("com.sec.android.app.sbrowser")

    /**
     * @return All possible chrome package names that provide custom tabs feature.
     */
    val packages: List<String>
        get() = listOf("", STABLE_PACKAGE, BETA_PACKAGE, DEV_PACKAGE, LOCAL_PACKAGE)

    private const val ACTION_CUSTOM_TABS_CONNECTION =
        "android.support.customtabs.action.CustomTabsService"

    private var sPackageNameToUse: String? = null

    /**
     * Builder function for [CustomTabsIntent], composable option
     * @param activity              - The activity to launch the CustomTab in
     * @param url                   - The browser url to open
     * @param session               - session to (re-)use
     * @param closeIcon             - asset to display for close action
     */
    @Composable
    fun openCustomTab(
        activity: Activity,
        url: String,
        session: CustomTabsSession?,
        closeIcon: Bitmap = with(Icons.Default.Close) {
            ImageBitmap(
                width = this.defaultWidth.value.toInt(),
                height = this.defaultHeight.value.toInt(),
            ).asAndroidBitmap()
        }
    ) = CustomTabActivityHelper.openCustomTab(
        activity,
        url,
        session,
        closeIcon
    )

    /**
     * Goes through all apps that handle VIEW intents and have a warmup service. Picks
     * the one chosen by the user if there is one, otherwise makes a best effort to return a
     * valid package name.
     *
     * This is **not** threadsafe.
     *
     * @param context [Context] to use for accessing [PackageManager].
     * @return The package name recommended to use for connecting to custom tabs related components.
     */
    fun getPackageNameToUse(context: Context): String? {
        if (sPackageNameToUse != null) return sPackageNameToUse
        val pm = context.packageManager
        // Get default VIEW intent handler.
        val activityIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.example.com"))
        val defaultViewHandlerInfo = pm.resolveActivity(activityIntent, 0)
        var defaultViewHandlerPackageName: String? = null
        if (defaultViewHandlerInfo != null) {
            defaultViewHandlerPackageName = defaultViewHandlerInfo.activityInfo.packageName
        }

        // Get all apps that can handle VIEW intents. (require <queries> block in manifest)
        // see https://developer.android.com/training/package-visibility/use-cases#check-browser-available
        val resolvedActivityList = pm.queryIntentActivities(activityIntent, 0)
        val packagesSupportingCustomTabs: MutableList<String?> = ArrayList()
        for (info in resolvedActivityList) {
            val serviceIntent = Intent()
            serviceIntent.action = ACTION_CUSTOM_TABS_CONNECTION
            serviceIntent.setPackage(info.activityInfo.packageName)
            if (pm.resolveService(serviceIntent, 0) != null) {
                packagesSupportingCustomTabs.add(info.activityInfo.packageName)
            }
        }

        // Now packagesSupportingCustomTabs contains all apps that can handle both VIEW intents
        // and service calls.
        if (packagesSupportingCustomTabs.isEmpty()) {
            sPackageNameToUse = null
        } else if (packagesSupportingCustomTabs.size == 1) {
            sPackageNameToUse = packagesSupportingCustomTabs[0]
        } else if (!TextUtils.isEmpty(defaultViewHandlerPackageName)
            && !hasSpecializedHandlerIntents(context, activityIntent)
            && packagesSupportingCustomTabs.contains(defaultViewHandlerPackageName)
        ) {
            sPackageNameToUse = defaultViewHandlerPackageName
        } else if (packagesSupportingCustomTabs.contains(STABLE_PACKAGE)) {
            sPackageNameToUse = STABLE_PACKAGE
        } else if (packagesSupportingCustomTabs.contains(BETA_PACKAGE)) {
            sPackageNameToUse = BETA_PACKAGE
        } else if (packagesSupportingCustomTabs.contains(DEV_PACKAGE)) {
            sPackageNameToUse = DEV_PACKAGE
        } else if (packagesSupportingCustomTabs.contains(LOCAL_PACKAGE)) {
            sPackageNameToUse = LOCAL_PACKAGE
        }
        return sPackageNameToUse
    }

    /**
     * Used to check whether there is a specialized handler for a given intent.
     * @param intent The intent to check with.
     * @return Whether there is a specialized handler for the given intent.
     */
    private fun hasSpecializedHandlerIntents(context: Context, intent: Intent): Boolean {
        try {
            val pm = context.packageManager
            val handlers = pm.queryIntentActivities(
                intent,
                PackageManager.GET_RESOLVED_FILTER
            )
            if (handlers.size == 0) {
                return false
            }
            for (resolveInfo in handlers) {
                val filter = resolveInfo.filter ?: continue
                if (filter.countDataAuthorities() == 0 || filter.countDataPaths() == 0) continue
                if (resolveInfo.activityInfo == null) continue
                return true
            }
        } catch (e: RuntimeException) {
            Timber.tag(TAG).e("Runtime exception while getting specialized handlers")
        }
        return false
    }
}