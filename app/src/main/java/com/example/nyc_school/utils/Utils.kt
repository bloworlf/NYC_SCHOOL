package com.example.nyc_school.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils


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
//        val gmmIntentUri = Uri.parse("google.navigation:q=$lat,$lon")
//        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
//        mapIntent.setPackage("com.google.android.apps.maps")
//        this.startActivity(mapIntent)

        val gmmIntentUri = Uri.parse(
            "geo:$lat,$lon${
                place?.let {
                    "?q$it"
                }
            }"
        )
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        mapIntent.resolveActivity(packageManager)?.let {
            startActivity(mapIntent)
        }
    }
}