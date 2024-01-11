package de.rauschdo.eschistory.utility

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build

fun Context.linkOut(url: String) {
    var intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
        addCategory(Intent.CATEGORY_BROWSABLE)
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        intent.flags = Intent.FLAG_ACTIVITY_REQUIRE_NON_BROWSER or
                Intent.FLAG_ACTIVITY_REQUIRE_DEFAULT
    } else {
        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY or
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK
    }
    if (this !is Activity) {
        intent = intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    try {
        startActivity(intent, null)
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
    }
}