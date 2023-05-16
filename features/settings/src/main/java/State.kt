package com.magicpark.features.settings




sealed class SupportState  {
    /* Login */
    object HelpRequestSent : SupportState()
    class HelpRequestError(val message: String? = null) : SupportState()
}