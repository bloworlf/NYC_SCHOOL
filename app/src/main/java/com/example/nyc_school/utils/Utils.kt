package com.example.nyc_school.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsService
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


object Utils {

    /**
     * Checks if a [Color][Color] is dark or not
     * @return true if the luminance is less than 0.5
     */
    fun Color.isDark(): Boolean {
        return this.luminance() < .5f
    }

    fun Int.isDark(): Boolean {
        if (this == 0) {
            return false
        }
        return ColorUtils.calculateLuminance(this) < .5f
    }

    fun hasPermissions(context: Context?, permissions: Array<String>): Boolean {
        if (context != null && permissions != null) {
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        permission!!
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }

    /**
     * Opens Google Maps and add a marker to the desired [latitude][lat] and [longitude][lon]
     * @param lat the desired latitude
     * @param lon the desired longitude
     * @param place the name of the marker
     */
    fun Context.openMaps(lat: String, lon: String, place: String? = null) {
//        val uri = "geo:$lat,$lon${place?.let { "?q$it" }}"
        val uri = "geo:0,0?q=$lat,$lon${
            place?.let {
                "(${
                    URLEncoder.encode(
                        it,
                        StandardCharsets.UTF_8.toString()
                    )
                })"
            }
        }"
//        val uri = "http://maps.google.com/maps?f=d&hl=en&saddr=$lat,$lon"

        val gmmIntentUri = Uri.parse(uri)
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        mapIntent.resolveActivity(packageManager)?.let {
            startActivity(mapIntent)
        }
    }

    private fun getCustomTabsPackages(context: Context, url: Uri?): MutableList<ResolveInfo> {
        val pm = context.packageManager

        val activityIntent = Intent(Intent.ACTION_VIEW, url)

        val resolvedActivityList = pm.queryIntentActivities(activityIntent, 0)
        val packagesSupportingCustomTabs: MutableList<ResolveInfo> = mutableListOf()
        for (info in resolvedActivityList) {
            val serviceIntent = Intent()
            serviceIntent.action = CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION
            serviceIntent.setPackage(info.activityInfo.packageName)

            if (pm.resolveService(serviceIntent, 0) != null) {
                packagesSupportingCustomTabs.add(info)
            }
        }
        return packagesSupportingCustomTabs
    }

    fun Context.openUrl(url: String, color: Color = Color.Black) {
        if (getCustomTabsPackages(this, Uri.parse(url)).size > 0) {
            val builder: CustomTabsIntent.Builder = CustomTabsIntent.Builder()
            builder.setNavigationBarColor(color.toArgb())
            builder.setToolbarColor(color.toArgb())
            builder.setShowTitle(true)
            val customTabsIntent: CustomTabsIntent = builder.build()
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://"))
            val resolveInfo = packageManager.resolveActivity(
                browserIntent,
                PackageManager.MATCH_DEFAULT_ONLY
            )
            if ((resolveInfo != null) && resolveInfo.activityInfo.packageName.isNotEmpty()) {
                customTabsIntent.intent.setPackage(resolveInfo.activityInfo.packageName)
            }
            url?.let {
                customTabsIntent.launchUrl(this, Uri.parse(url))
            }

        } else {
            Toast.makeText(
                this,
                "Couldn't find an app to open the web page.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}