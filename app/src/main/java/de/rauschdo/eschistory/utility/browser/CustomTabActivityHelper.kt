package de.rauschdo.eschistory.utility.browser

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.widget.Toast
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession
import de.rauschdo.eschistory.utility.linkOut

/**
 * This is a helper class to manage the connection to the Custom Tabs Service.
 */
class CustomTabActivityHelper(private val activity: Activity) : ServiceConnectionCallback {

    private var mCustomTabsSession: CustomTabsSession? = null

    private var mClient: CustomTabsClient? = null

    private var mConnection: CustomTabsServiceConnection? = null

    private var mConnectionCallback: ConnectionCallback? = null

    /**
     * Creates or retrieves an exiting CustomTabsSession.
     *
     * @return a CustomTabsSession.
     */
    val session: CustomTabsSession?
        get() {
            if (mClient == null) {
                mCustomTabsSession = null
            } else if (mCustomTabsSession == null) {
                mCustomTabsSession = mClient?.newSession(null)
            }
            return mCustomTabsSession
        }

    companion object {
        /**
         * Opens the URL on a Custom Tab if possible. Otherwise fallsback to opening it on a WebView.
         *
         * @param activity The host activity.
         * @param url the Url to be opened.
         * @param session session to (re-)use
         * @param closeIcon asset to display for close action
         */
        fun openCustomTab(
            activity: Activity,
            url: String,
            session: CustomTabsSession?,
            closeIcon: Bitmap?
        ) {
            val packageName = CustomTabsHelper.getPackageNameToUse(activity)
            val uri = Uri.parse(url)

            val fallback = {
                with(activity) {
                    Toast.makeText(
                        this,
                        "Keine Option für In-App Browsing gefunden.\nFallback auf externen Browser",
                        Toast.LENGTH_SHORT
                    ).show()
                    activity.linkOut(url)
                }
            }

            //If we cant find a package name, it means theres no browser that supports
            //Chrome Custom Tabs installed. So, we fallback to the linkout
            if (packageName == null) {
                fallback()
            } else {
                with(
                    activity.buildCustomTabsIntent(
                        session,
                        closeIcon
                    )
                ) {
                    intent.setPackage(packageName)
                    //This can easily crash if url in data object has no protocol
                    //i.e "www.google.com" instead of "http(s)://www.google.com" which is correct
                    try {
                        launchUrl(activity, uri)
                    } catch (e: ActivityNotFoundException) {
                        fallback()
                    }
                }
            }
        }

        private fun Context.buildCustomTabsIntent(
            session: CustomTabsSession?,
            closeIcon: Bitmap?
        ) = CustomTabsIntent.Builder().apply {
            setDefaultColorSchemeParams(
                CustomTabColorSchemeParams.Builder().apply {
                    setNavigationBarColor(Color.WHITE)
                    setNavigationBarDividerColor(Color.BLACK)
                    setToolbarColor(Color.WHITE)
                }.build()
            )

            setShareState(CustomTabsIntent.SHARE_STATE_ON)
            setShowTitle(true)
            setUrlBarHidingEnabled(false)

            session?.let { setSession(it) }

            // back button icon (can't set color)
            closeIcon?.let {
                setCloseButtonIcon(it)
            }

            // animation for enter and exit of tab
            setStartAnimations(
                this@buildCustomTabsIntent,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            setExitAnimations(
                this@buildCustomTabsIntent,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
        }.build()
    }

    /**
     * Unbinds the Activity from the Custom Tabs Service
     */
    fun unbindCustomTabsService() {
        mConnection?.let {
            activity.unbindService(it)
        }
        mClient = null
        mCustomTabsSession = null
        mConnection = null
    }

    /**
     * Register a Callback to be called when connected or disconnected from the Custom Tabs Service.
     */
    fun setConnectionCallback(connectionCallback: ConnectionCallback?) {
        mConnectionCallback = connectionCallback
    }

    /**
     * Binds the Activity to the Custom Tabs Service.
     */
    fun bindCustomTabsService() {
        if (mClient != null) return
        val packageName: String = CustomTabsHelper.getPackageNameToUse(activity) ?: return
        ServiceConnection(this).apply {
            mConnection = this
            CustomTabsClient.bindCustomTabsService(activity, packageName, this)
        }
    }

    override fun onServiceConnected(client: CustomTabsClient?) {
        mClient = client
        mClient?.warmup(0L)
        if (mConnectionCallback != null) mConnectionCallback?.onCustomTabsConnected()
    }

    override fun onServiceDisconnected() {
        mClient = null
        mCustomTabsSession = null
        if (mConnectionCallback != null) mConnectionCallback?.onCustomTabsDisconnected()
    }

    /**
     * A Callback for when the service is connected or disconnected. Use those callbacks to
     * handle UI changes when the service is connected or disconnected.
     */
    interface ConnectionCallback {
        /**
         * Called when the service is connected.
         */
        fun onCustomTabsConnected()

        /**
         * Called when the service is disconnected.
         */
        fun onCustomTabsDisconnected()
    }
}