package com.magicpark.data.session

import android.content.Context
import android.content.SharedPreferences

class MagicparkDbSession(context: Context) {

    private var prefs: SharedPreferences =
        context.getSharedPreferences(KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        val editor = prefs.edit()
        editor.putString(KEY_API_TOKEN, token)
        editor.apply()
    }

    fun getToken(): String {
        return prefs.getString(KEY_API_TOKEN, null) ?: throw Exception("The device does not have a token")
    }





    companion object {
        const val KEY_SHARED_PREFERENCES = "KEY-MAGICPARK"
        const val KEY_API_TOKEN = "KEY-API-TOKEN"

        const val KEY_CART = "KEY-CART"
    }
}
