package com.magicpark.domain.usecases

import android.content.ContentValues.TAG
import android.util.Log
import android.util.Patterns
import java.util.regex.Pattern

class RegisterUseCases {

    private companion object {
        const val REGEX_FULLNAME = "[A-Z](?=.{1,29}$)[A-Za-z]{1,}([ ][A-Z][A-Za-z]{1,})*"
    }

    fun isValidEmail(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    fun checkFields(
        mail: String,
        password: String,
        passwordConfirmation: String,
        fullName: String,
        phoneNumber: String,
        country: String,
        cgvChecked: Boolean
    ) {
        Log.d(
            TAG,
            "Register with mail = ${mail}, fullName = ${fullName}, phoneNumber = ${phoneNumber}, mail = ${mail}, password = $password, country = ${country}"
        )

        val pattern = Pattern.compile(REGEX_FULLNAME)
        val matcher = pattern.matcher(fullName)


        when {
            mail.isEmpty() -> ""
            password.isEmpty() -> ""
            passwordConfirmation.isEmpty() -> ""
            fullName.isEmpty() -> ""
            phoneNumber.isEmpty() -> ""

            !matcher.matches() -> ""
            !isValidEmail(mail) -> ""
            passwordConfirmation != password -> ""
            !cgvChecked -> ""
        }
    }

        /*
        if (mail.isEmpty()
            || password.isEmpty()
            || passwordConfirmation.isEmpty()
            || fullName.isEmpty()
            || phoneNumber.isEmpty()
        )
            return@launch showRegisterError(R.string.common_empty_fields)

        if (!matcher.matches()) return@launch showRegisterError(R.string.register_error_fullname)
        if (!isValidEmail(mail)) return@launch showRegisterError(R.string.register_error_wrong_email)
        if (passwordConfirmation != password) return@launch showRegisterError(R.string.register_error_no_matching_passwords)
        if (!cgvChecked) return@launch showRegisterError(R.string.register_error_terms_not_accepted)
    }

         */
}
