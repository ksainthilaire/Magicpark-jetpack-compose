package com.magicpark.domain.usecases

import com.magicpark.domain.model.magicpark.ApplicationConfiguration
import com.magicpark.domain.repositories.ISettingsRepository
import javax.inject.Inject

class SettingsUseCases(@Inject val repository: ISettingsRepository) {
    suspend fun getSettings(): ApplicationConfiguration =
        repository.getSettings()
}
