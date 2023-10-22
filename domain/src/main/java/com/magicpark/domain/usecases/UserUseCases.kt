package com.magicpark.domain.usecases

import com.magicpark.domain.model.*
import com.magicpark.domain.repositories.IUserRepository
import javax.inject.Inject

class UserUseCases @Inject constructor(private val repository: IUserRepository) {

    suspend fun login(firebaseToken: String): String =
        repository.login(firebaseToken)

    suspend fun updateUser(
        mail: String?,
        fullName: String?,
        phoneNumber: String?,
        avatarUrl: String?,
        country: String?
    ): Unit =
        repository.updateUser(
            mail = mail,
            fullName = fullName,
            phoneNumber = phoneNumber,
            avatarUrl = avatarUrl,
            country = country,
        )

    suspend fun getUser(): User =
        repository.getUser()

    suspend fun deleteUser(): Unit =
        repository.deleteUser()

    suspend fun logout() : Unit =
        repository.logout()
}
