package com.example.nyc_school.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


object Utils {

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

    fun Context.openMaps(lat: String, lon: String, place: String? = null) {
//        val uri = "geo:$lat,$lon${place?.let { "?q$it" }}"
        val uri = "geo:0,0?q=$lat,$lon${place?.let { "(${URLEncoder.encode(it, StandardCharsets.UTF_8.toString())})" }}"
//        val uri = "http://maps.google.com/maps?f=d&hl=en&saddr=$lat,$lon"

        val gmmIntentUri = Uri.parse(uri)
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        mapIntent.resolveActivity(packageManager)?.let {
            startActivity(mapIntent)
        }
    }
}