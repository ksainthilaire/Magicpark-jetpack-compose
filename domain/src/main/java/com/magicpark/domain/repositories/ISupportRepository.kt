package com.magicpark.domain.repositories

import com.magicpark.domain.enums.PaymentMethodEnum
import com.magicpark.domain.model.*
import com.magicpark.domain.usecases.OrderUseCases
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Header

interface ISupportRepository {
    fun help(text: String): Completable
}