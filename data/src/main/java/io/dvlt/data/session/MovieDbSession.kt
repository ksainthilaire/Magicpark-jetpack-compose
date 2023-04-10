package io.dvlt.data.session

import android.content.Context
import android.content.SharedPreferences
import io.dvlt.core.Config

class MovieDbSession(context: Context) {

    private var prefs: SharedPreferences =
        context.getSharedPreferences(KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        val editor = prefs.edit()
        editor.putString(KEY_API_TOKEN, token)
        editor.apply()
    }

    fun getToken(): String {
            // return prefs.getString(KEY_API_TOKEN, null) ?: throw Exception("The device does not have a token")
        return Config.tmdbApiKeyV3
    }


    companion object {
        const val KEY_SHARED_PREFERENCES = "KEY-DVLT"
        const val KEY_API_TOKEN = "KEY-API-TOKEN"
    }
}
