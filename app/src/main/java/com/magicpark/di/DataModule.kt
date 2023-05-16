package com.magicpark.core.di

import android.app.Application
import android.content.res.Resources
import androidx.room.Room
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import com.magicpark.core.Config
import com.magicpark.data.api.MagicparkApi
import com.magicpark.data.local.AppDatabase
import com.magicpark.data.local.ShopDao
import com.magicpark.data.local.UserTicketDao
import com.magicpark.data.repositories.*
import com.magicpark.domain.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Singleton
    @Provides
    fun provideResources(androidApplication: Application): Resources {
        return androidApplication.resources
    }

    @Provides
    @Singleton
    fun providesDatabase(androidApplication: Application): AppDatabase {
        return Room.databaseBuilder(
            androidApplication,
            AppDatabase::class.java, "database-magicpark"
        ).build()
    }

    @Provides
    @Singleton
    fun providesOkHttp(): OkHttpClient {
        val logging = HttpLoggingInterceptor();

        return OkHttpClient().newBuilder().addInterceptor(logging).callTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun providesRetrofit(httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Config.apiUrl)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMagicparkApi(retrofit: Retrofit): MagicparkApi {
        return retrofit.create(MagicparkApi::class.java)
    }

    @Provides
    @Singleton
    fun provideShopDao(database: AppDatabase): ShopDao {
        return database.shopDao()
    }

    @Provides
    @Singleton
    fun provideTicketDao(database: AppDatabase): UserTicketDao {
        return database.ticketDao()
    }



    @Provides
    @Singleton
    fun providesOrderRepository(magicparkApi: MagicparkApi): OrderRepository {
        return OrderRepository(magicparkApi)
    }

    @Provides
    @Singleton
    fun providesShopRepository(shopDao: ShopDao, magicparkApi: MagicparkApi): ShopRepository {
        return ShopRepository(shopDao, magicparkApi)
    }

    @Provides
    @Singleton
    fun providesTicketRepository(
        ticketDao: UserTicketDao,
        magicparkApi: MagicparkApi
    ): TicketRepository {
        return TicketRepository(ticketDao, magicparkApi)
    }

    @Provides
    @Singleton
    fun providesSupportTicket(magicparkApi: MagicparkApi): SupportRepository {
        return SupportRepository(magicparkApi)
    }

    @Provides
    @Singleton
    fun providesSettingsRepository(magicparkApi: MagicparkApi): SettingsRepository {
        return SettingsRepository(magicparkApi)
    }


    @Provides
    @Singleton
    fun providesOrderUseCase(repository: OrderRepository) = OrderUseCases(repository)

    @Provides
    @Singleton
    fun providesSettingsUseCase(repository: SettingsRepository) = SettingsUseCases(repository)

    @Provides
    @Singleton
    fun providesShopUseCase(repository: ShopRepository) = ShopUseCases(repository)

    @Provides
    @Singleton
    fun providesSupportUseCase(repository: SupportRepository) = SupportUseCases(repository)

    @Provides
    @Singleton
    fun providesTicketUseCase(repository: TicketRepository) = TicketUseCases(repository)

    @Provides
    @Singleton
    fun providesUserUseCase(repository: UserRepository) = UserUseCases(repository)
}


val dataModule = module {

    /*
    single<MovieDbSession> {
        MovieDbSession(androidContext())
    }

     */
}