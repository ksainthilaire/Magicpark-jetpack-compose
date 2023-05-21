package com.magicpark.data.repositories

import com.magicpark.data.api.MagicparkApi
import com.magicpark.domain.model.magicpark.ApplicationConfiguration
import com.magicpark.domain.repositories.ISettingsRepository
import io.reactivex.rxjava3.core.*
import javax.inject.Inject

class SettingsRepository @Inject constructor(var magicparkApi: MagicparkApi) : ISettingsRepository {

    override fun getSettings(): Observable<ApplicationConfiguration> {
        return magicparkApi.getSettings().map {
            ApplicationConfiguration(
                appUrl = it.appUrl,
                appVersionName = it.appVersionName
            )
        }
    }

}