package com.magicpark.data.utils

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class GoogleSignInActivityContract : ActivityResultContract<
        GoogleSignInOptions,
        GoogleSignInActivityContract.Result
        >() {

    override fun createIntent(context: Context, input: GoogleSignInOptions): Intent {
        val client = GoogleSignIn.getClient(context, input ?: error("No GoogleSignInOptions Provided"))
        return client.signInIntent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Result {
        return handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(intent))
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>): Result {
        return try {
            Result.Success(completedTask.result)
        } catch (e: Exception) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            (e as? ApiException)?.let { println("signInResult:failed code=" + it.statusCode)  }
            Result.Failure(e)
        }
    }

    sealed class Result {
        data class Success(val googleSignInAccount: GoogleSignInAccount) : Result()
        data class Failure(val exception: Exception) : Result()
    }
}