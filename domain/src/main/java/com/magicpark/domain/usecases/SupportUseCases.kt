package com.magicpark.domain.usecases

import com.magicpark.domain.repositories.ISupportRepository
import com.magicpark.domain.repositories.IUserRepository
import io.reactivex.rxjava3.core.Completable
import org.koin.java.KoinJavaComponent.inject
import javax.inject.Inject

class SupportUseCases(@Inject val repository: ISupportRepository) {
    suspend fun help(message: String): Unit = repository.help(message)
}
