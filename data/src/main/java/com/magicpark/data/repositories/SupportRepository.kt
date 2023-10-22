package com.magicpark.data.repositories

import com.magicpark.data.api.MagicparkApi
import com.magicpark.data.model.request.HelpRequest
import com.magicpark.domain.repositories.ISupportRepository
import org.koin.java.KoinJavaComponent
import java.util.concurrent.TimeUnit

class SupportRepository : ISupportRepository {

    private val magicparkApi: MagicparkApi by KoinJavaComponent.inject(MagicparkApi::class.java)

    override suspend fun help(text: String): Boolean {
        val request = HelpRequest(message = text)

        return magicparkApi
            .help(request)
            .blockingAwait(200L, TimeUnit.SECONDS)
    }
}
