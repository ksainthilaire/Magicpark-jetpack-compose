package com.magicpark.features.account

import com.magicpark.domain.model.Movie

sealed class AccountState  {
    object DeletionFailed : AccountState()
    class DeletionSuccess : AccountState()
    object SuccessfulDeletion : AccountState()
}
