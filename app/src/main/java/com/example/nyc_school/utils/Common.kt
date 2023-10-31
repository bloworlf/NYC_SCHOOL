package com.example.nyc_school.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager

object Common {

    val DEFAULT_LANGUAGE: String = "en"
    val DEFAULT_THEME: Int = AppCompatDelegate.MODE_NIGHT_NO

    fun preferences(context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    object PREFERENCES {
        val LANGUAGE_KEY: String = "LANGUAGE_KEY"
        val USER_KEY: String = "USER_KEY"
        val WELCOME_SCREEN_KEY: String = "WELCOME_SCREEN_KEY"
        val SPONSORED_KEY: String = "SPONSORED_KEY"
        val DISPLAY_TYPE_KEY: String = "DISPLAY_TYPE_KEY"
        val THEME_KEY: String = "THEME_KEY"
        val EMULATE_SELF_KEY: String = "EMULATE_SELF_KEY"
        val DISABLE_REQUEST: String = "DISABLE_REQUEST"
    }

    object API {
        //https://data.cityofnewyork.us/resource/s3k6-pzi2.json
        const val BASE_URL = "https://data.cityofnewyork.us/resource/"
//        const val API_VERSION =
        const val SCHOOL_INFO = BASE_URL + "s3k6-pzi2.json"
//        const val SAT_SCORE = "https://data.cityofnewyork.us/resource/f9bf-2cp4.json"
        const val SAT_SCORE = "https://data.cityofnewyork.us/resource/f9bf-2cp4.json"
    }
}