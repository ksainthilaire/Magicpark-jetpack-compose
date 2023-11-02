package com.magicpark.data.repositories

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.magicpark.data.api.MagicparkApi
import com.magicpark.data.model.request.authentification.LoginRequest
import com.magicpark.data.model.request.authentification.UpdateUserRequest
import com.magicpark.domain.model.User
import com.magicpark.domain.repositories.IUserRepository
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.single
import org.koin.java.KoinJavaComponent


class UserRepository : IUserRepository {

    private companion object {
        val TAG: String = UserRepository::class.java.simpleName
    }

    private val api: MagicparkApi by KoinJavaComponent
        .inject(MagicparkApi::class.java)

    override suspend fun login(token: String): String {
        val request = LoginRequest(
            token = token
        )

        val response = api
            .login(request)

        return response.user?.token
            ?: throw Exception("Unable to get user token token = $token")
    }

    override suspend fun updateUser(
        mail: String?,
        fullName: String?,
        phoneNumber: String?,
        avatarUrl: String?,
        country: String?,
    ) {

        val request = UpdateUserRequest(
            mail = mail,
            fullName = fullName,
            phoneNumber = phoneNumber,
            avatarUrl = avatarUrl,
            country = country,
        )

        return api
            .updateUser(request)
            .subscribeOn(Schedulers.io())
            .doOnError { throwable ->
                Log.e(TAG, "Unable to update user", throwable)
            }
            .doOnSubscribe {
                Log.d(TAG, "Updating user with query $request")
            }
            .blockingAwait()
    }

    override suspend fun deleteUser() {
        api
            .deleteUser()
            .blockingAwait()
    }

    override suspend fun getUser(): User {
        val response = api
            .getUser()

        return response.user ?: throw Error("No token associated with the user")
    }

    override suspend fun logout() {
        Firebase.auth.signOut()
    }
}

