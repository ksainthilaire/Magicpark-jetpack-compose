package com.magicpark.data.repositories

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.magicpark.core.Config.SERVER_CLIENT_ID
import com.magicpark.data.api.MagicparkApi
import com.magicpark.data.model.request.authentification.LoginRequest
import com.magicpark.data.model.request.authentification.UpdateUserRequest
import com.magicpark.data.session.MagicparkDbSession
import com.magicpark.data.utils.GoogleSignInActivityContract
import com.magicpark.domain.model.*
import com.magicpark.domain.repositories.IUserRepository
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import org.koin.java.KoinJavaComponent


class UserRepository : IUserRepository {


    private val magicparkDbSession: MagicparkDbSession by KoinJavaComponent.inject(
        MagicparkDbSession::class.java
    )
    private val magicparkApi: MagicparkApi by KoinJavaComponent.inject(MagicparkApi::class.java)

    private val auth = FirebaseAuth.getInstance()

    private var googleSignInListener: ((result: GoogleSignInActivityContract.Result) -> Unit)? =
        null



    private lateinit var callbackManager: CallbackManager


    override fun loginWithFacebook(activity: AppCompatActivity): Observable<User> =
        PublishSubject.create() { publishSubject ->

            callbackManager = CallbackManager.Factory.create()
            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult) {
                        val accessToken = result.accessToken.token
                        handleLoginResult(publishSubject, Provider.FACEBOOK, accessToken)
                    }

                    override fun onCancel() {}
                    override fun onError(error: FacebookException) {
                        println("error $error")
                    }
                })


            LoginManager.getInstance()
                .logInWithReadPermissions(
                    activity,
                    callbackManager,
                    listOf("public_profile", "email")
                )

        }

    override fun forgot(mail: String): Completable {

        val completable = Completable.create { completable ->

            FirebaseAuth.getInstance().setLanguageCode("fr")
            FirebaseAuth.getInstance().sendPasswordResetEmail(mail)
                .addOnCompleteListener {

                }
                .addOnSuccessListener {
                    completable.onComplete()
                }.addOnFailureListener {
                    completable.onError(it)
                }
        }
        return completable
    }

    override fun loginWithGoogle(activity: AppCompatActivity): Observable<User> =
        PublishSubject.create() { publishSubject ->

            val resultLauncher =
                activity.registerForActivityResult(GoogleSignInActivityContract()) { result ->
                    googleSignInListener?.let { callback -> callback(result) }
                }

            googleSignInListener = { result ->
                when (result) {
                    is GoogleSignInActivityContract.Result.Success -> {
                        val googleSignInAccount = result.googleSignInAccount
                        val idToken = googleSignInAccount.idToken
                        if (!idToken.isNullOrEmpty()) {
                            val auth = FirebaseAuth.getInstance()
                            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)

                            auth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener(activity) { task ->
                                    if (task.isSuccessful) {
                                        auth.currentUser?.getIdToken(false)
                                            ?.addOnSuccessListener { result ->
                                                handleLoginResult(
                                                    publishSubject,
                                                    Provider.GOOGLE,
                                                    result.token.toString()
                                                )
                                            }
                                    } else {
                                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                                        task.exception?.let { publishSubject.onError(it) }
                                    }
                                }

                        }


                        Log.w(TAG, "signInWithCredential:failure")
                    }
                    is GoogleSignInActivityContract.Result.Failure -> {
                        Log.w(TAG, "signInWithCredential:failure", result.exception)
                        publishSubject.onError(result.exception)
                    }
                }
            }

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(
                    SERVER_CLIENT_ID
                )
                .requestEmail()
                .build()


            resultLauncher.launch(gso)
        }

    override fun loginWithMail(
        activity: AppCompatActivity,
        mail: String,
        password: String
    ): Observable<User> = PublishSubject.create { publishSubject ->

        auth.signInWithEmailAndPassword(mail, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.getIdToken(false)?.addOnSuccessListener { result ->
                        result.token?.let {
                            handleLoginResult(
                                publishSubject,
                                Provider.DEFAULT,
                                it,
                            )
                        } ?: publishSubject.onError(Throwable())
                    }

                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
            .addOnFailureListener {
                publishSubject.onError(it)
            }

    }

    override fun registerWithMail(
        activity: AppCompatActivity,
        fullName: String,
        phoneNumber: String,
        mail: String,
        password: String
    ): Observable<User> = PublishSubject.create { publishSubject ->


        auth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                user?.getIdToken(false)?.addOnSuccessListener { result ->
                    val token = result.token
                    if (token == null) {
                        publishSubject.onError(Throwable("auth.createUserWithEmailAndPassword"))
                    } else {

                        login(token)
                            .subscribe(
                                {
                                    updateUser(mail, fullName, phoneNumber, "", "fr")
                                        .doOnComplete {
                                            publishSubject.onNext(it)
                                        }
                                        .doOnError {
                                            publishSubject.onError(it)
                                        }
                                },
                                { throwable ->
                                    if (throwable is retrofit2.HttpException) {
                                        publishSubject.onError(throwable)
                                    }
                                }
                            )

                    }
                }
            }
        }.addOnFailureListener {
            publishSubject.onError(Throwable())
        }

    }


    fun handleLoginResult(
        publishSubject: ObservableEmitter<User>,
        provider: Provider,
        accessToken: String
    ) {
        Log.d(TAG, "provider=${provider.name} token=${accessToken}")
        val observable = when (provider) {
            Provider.DEFAULT ->
                login(accessToken)
            Provider.GOOGLE ->
                login(accessToken)
            else -> login(accessToken)
        }
        observable.subscribe(
            {
                publishSubject.onNext(it)
            },
            { throwable ->
                if (throwable is retrofit2.HttpException) {
                    publishSubject.onError(throwable)
                }
            }
        )
    }


    override fun logout(): Completable {
        return Completable.create {
            Firebase.auth.signOut()
            magicparkDbSession.clearToken()
            it.onComplete()
        }
    }


    override fun updateUser(
        mail: String?,
        fullname: String?,
        phoneNumber: String?,
        avatarUrl: String?,
        country: String?
    ): Completable {
        val request = UpdateUserRequest(
            mail = mail,
            fullName = fullname,
            phoneNumber = phoneNumber,
            avatarUrl = avatarUrl,
            country = country
        )
        return magicparkApi.updateUser(magicparkDbSession.getToken(), request)
            .subscribeOn(Schedulers.io())

    }

    override fun deleteUser(): Completable {
        return magicparkApi.deleteUser(magicparkDbSession.getToken())
            .subscribeOn(Schedulers.io())
    }

    override fun getUser(): Observable<User> {
        return magicparkApi.getUser(magicparkDbSession.getToken())
            .subscribeOn(Schedulers.io())
            .map {
                it.user ?: throw Error("No token associated with the user")
            }
    }

    private fun login(token: String): Observable<User> {
        val request = LoginRequest(
            token = token
        )
        return magicparkApi.login(request)
            .subscribeOn(Schedulers.io()).map {
                it.user?.let(::saveToken) ?: throw Error("No token associated with the user")
            }
    }

    private fun saveToken(user: User): User {
        user.token?.let { token ->
            magicparkDbSession.saveToken(token)
        }
        return user
    }

    companion object {
        const val TAG = "UserRepository"
    }


    enum class Provider {
        DEFAULT,
        FACEBOOK,
        GOOGLE
    }
}

