package com.magicpark.core.di

import android.app.Application
import android.content.Context
import android.content.res.Resources
import androidx.room.Room
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import com.magicpark.core.Config
import com.magicpark.data.api.MagicparkApi
import com.magicpark.data.local.AppDatabase
import com.magicpark.data.local.ShopDao
import com.magicpark.data.local.UserTicketDao
import com.magicpark.data.repositories.*
import com.magicpark.data.session.MagicparkDbSession
import com.magicpark.domain.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule(private val application: Application) {


    @Provides
    @Singleton
    fun providesApplication(): Application = application

    @Provides
    @Singleton
    fun providesApplicationContext(): Context = application


    @Singleton
    @Provides
    fun provideResources(): Resources {
        return application.resources
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
    fun providesOrderRepository(
        magicparkApi: MagicparkApi,
        magicparkDbSession: MagicparkDbSession
    ): OrderRepository {
        return OrderRepository(magicparkApi, magicparkDbSession)
    }

    @Provides
    @Singleton
    fun providesShopRepository(
        shopDao: ShopDao,
        magicparkApi: MagicparkApi,
        magicparkDbSession: MagicparkDbSession
    ): ShopRepository {
        return ShopRepository(shopDao, magicparkApi, magicparkDbSession)
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
    fun providesSupportTicket(
        magicparkApi: MagicparkApi,
        magicparkDbSession: MagicparkDbSession
    ): SupportRepository {
        return SupportRepository(magicparkApi, magicparkDbSession)
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

    @Provides
    @Singleton
    fun providesMagicparkDbSession(context: Context): MagicparkDbSession =
        MagicparkDbSession(context)
}