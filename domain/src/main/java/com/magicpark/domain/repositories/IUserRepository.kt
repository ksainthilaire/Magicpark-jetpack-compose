package com.magicpark.domain.repositories

import com.magicpark.domain.enums.LoginProvider
import com.magicpark.domain.model.User
import io.reactivex.rxjava3.core.Completable
import kotlinx.coroutines.flow.Flow

interface IUserRepository {

    /**
     * @param token
     */
    suspend fun login(
        token: String,
    ): String

    /**
     *
     */
    suspend fun logout()

    /**
     *
     * @param mail
     * @param fullName
     * @param phoneNumber
     * @param avatarUrl
     * @param country
     */
    suspend fun updateUser(
        mail: String? = null,
        fullName: String? = null,
        phoneNumber: String? = null,
        avatarUrl: String? = null,
        country: String? = null,
    )

    /**
     *
     */
    suspend fun deleteUser()

    /**
     *
     */
    suspend fun getUser(): User
}
