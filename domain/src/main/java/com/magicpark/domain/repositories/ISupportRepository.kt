package com.magicpark.domain.repositories

import io.reactivex.rxjava3.core.Completable

interface ISupportRepository {
    suspend fun help(text: String): Unit
}
