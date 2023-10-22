package com.magicpark.data.repositories

import com.magicpark.data.api.MagicparkApi
import com.magicpark.domain.model.magicpark.ApplicationConfiguration
import com.magicpark.domain.repositories.ISettingsRepository
import kotlinx.coroutines.flow.single
import javax.inject.Inject

class SettingsRepository @Inject constructor(var api: MagicparkApi) : ISettingsRepository {

    override suspend fun getSettings(): ApplicationConfiguration {
        val response = api
            .getSettings()
            .single()

        return ApplicationConfiguration(
            appUrl = response.appUrl,
            appVersionName = response.appVersionName,
        )
    }
}
