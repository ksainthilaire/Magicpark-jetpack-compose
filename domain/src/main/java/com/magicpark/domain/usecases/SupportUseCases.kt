package com.magicpark.domain.usecases

import com.magicpark.domain.repositories.ISupportRepository
import com.magicpark.domain.repositories.IUserRepository
import io.reactivex.rxjava3.core.Completable
import org.koin.java.KoinJavaComponent.inject

class SupportUseCases(val repository: ISupportRepository){
    fun help(message: String): Completable = repository.help(message)
}