package com.magicpark.features.login

/**
 *
 */
sealed interface LoginUiState {


    /**
     *
     */
    sealed interface LoginScreen : LoginUiState {


        /**
         *
         */
        enum class LoginError {
            //
            BAD_CREDENTIALS,
            //
            EMPTY_FIELDS,
            //
            UNKNOWN,
        }

        /**
         *
         */
        object Empty : LoginScreen

        /**
         *
         */
        object LoginSuccessful : LoginScreen

        /**
         *
         */
        class LoginFailed(val error: LoginError) : LoginScreen

    }

    /**
     *
     */
    sealed interface RegisterScreen : LoginUiState {

        /**
         *
         */

        enum class RegisterError {
            //
            BAD_CREDENTIALS,

            //
            EMPTY
        }

        /**
         *
         */

        class RegisterFailed(val error: RegisterError) : RegisterScreen

        /**
         *
         */

        object RegisterSuccessful : RegisterScreen
    }

    /**
     *
     */

    sealed interface ForgotScreen : LoginUiState {


        /**
         *
         */
        enum class ForgotError {
            //
            WRONG_EMAIL,

            //
            UNKNOWN,
        }

        /**
         *
         */

        object ForgotSuccessful : ForgotScreen

        /**
         *
         */

        class ForgotFailed(val error: ForgotError) : ForgotScreen
    }
}

