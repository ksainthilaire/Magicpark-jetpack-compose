package com.magicpark.features.login.utils

import android.util.Log
import com.google.firebase.auth.FirebaseAuthException
import com.magicpark.utils.R

fun FirebaseAuthException.getStringRes(): Int {
    return when (errorCode) {
        "ERROR_INVALID_EMAIL" -> R.string.login_failed
        else -> {
            Log.d(FirebaseAuthException::class.java.simpleName, "$errorCode")
            R.string.login_failed
        }
    }
}
