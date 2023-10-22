package com.magicpark.features.settings




sealed interface  SettingsState  {

    object Loading : SettingsState
    object HelpRequestSent : SettingsState

    class HelpRequestError(val message: String? = null) : SettingsState

    object LogoutSucceeded : SettingsState
}
