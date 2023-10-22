package com.magicpark.features.account



/**
 *
 */
sealed interface AccountState  {

    /**
     *
     */
    object Idle : AccountState

    /**
     *
     */
    object DeletionFailed : AccountState

    /**
     *
     */
    class DeletionSuccess : AccountState

    /**
     *
     */
    object SuccessfulDeletion : AccountState
}
