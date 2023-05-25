package com.magicpark.features.settings




sealed class SettingsState  {
    /* Login */
    object HelpRequestSent : SettingsState()
    class HelpRequestError(val message: String? = null) : SettingsState()

    object LogoutSucceeded : SettingsState()
}