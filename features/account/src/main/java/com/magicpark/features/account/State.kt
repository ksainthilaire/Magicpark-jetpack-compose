package com.magicpark.features.account



sealed class AccountState  {
    object DeletionFailed : AccountState()
    class DeletionSuccess : AccountState()
    object SuccessfulDeletion : AccountState()
}
