package com.magicpark.domain.usecases


import com.magicpark.domain.model.magicpark.ApplicationConfiguration
import com.magicpark.domain.repositories.ISettingsRepository
import com.magicpark.domain.repositories.IUserRepository
import io.reactivex.rxjava3.core.Observable

class SettingsUseCases(val repository: ISettingsRepository) {
    fun getSettings(): Observable<ApplicationConfiguration> = repository.getSettings()
}
