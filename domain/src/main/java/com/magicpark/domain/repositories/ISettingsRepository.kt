package com.magicpark.domain.repositories

import com.magicpark.domain.model.magicpark.ApplicationConfiguration
import io.reactivex.rxjava3.core.Observable

interface ISettingsRepository {
    fun getSettings(): Observable<ApplicationConfiguration>
}