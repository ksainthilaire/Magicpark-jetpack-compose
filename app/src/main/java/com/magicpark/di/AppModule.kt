package com.magicpark.core.di

import android.app.Application
import android.content.Context
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import com.magicpark.core.Config
import com.magicpark.data.api.MagicparkApi
import com.magicpark.data.local.AppDatabase
import com.magicpark.data.local.ShopDao
import com.magicpark.data.local.UserTicketDao
import com.magicpark.data.repositories.*
import com.magicpark.data.session.MagicparkDbSession
import com.magicpark.domain.repositories.*
import com.magicpark.domain.usecases.*
import com.magicpark.features.login.LoginViewModel
import com.magicpark.features.settings.SettingsViewModel
import com.magicpark.features.shop.ShopViewModel
import com.magicpark.features.wallet.WalletViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


val AppModule = module {

    single<Resources> {
        androidContext().resources
    }

    viewModel { LoginViewModel() }
    viewModel { ShopViewModel() }
    viewModel { WalletViewModel() }
    viewModel { SettingsViewModel() }


    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java, "database-magicpark"
        ).build()
    }

    single {
        val logging = HttpLoggingInterceptor();

        OkHttpClient().newBuilder().addInterceptor(logging)
            .callTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    single {

        val httpClient = get<OkHttpClient>()

        Retrofit.Builder()
            .baseUrl(Config.API_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    single<MagicparkApi> {
        get<Retrofit>().create(MagicparkApi::class.java)
    }

    single<ShopDao> {
        val database = get<AppDatabase>()
        database.shopDao()
    }

    single<UserTicketDao> {
        val database = get<AppDatabase>()
        database.ticketDao()
    }


    single<IShopRepository> { ShopRepository() }
    single<IUserRepository> { UserRepository() }
    single<ISupportRepository> { SupportRepository() }
    single<ITicketRepository> { TicketRepository() }
    single<ISettingsRepository> { SettingsRepository(get()) }
    single<IOrderRepository> { OrderRepository() }
    single<MagicparkDbSession> { MagicparkDbSession(get()) }









    single<ShopUseCases> { ShopUseCases(get()) }
    single<UserUseCases> { UserUseCases(get()) }
    single<SupportUseCases> { SupportUseCases(get()) }
    single<TicketUseCases> { TicketUseCases(get()) }
    single<SettingsUseCases> { SettingsUseCases(get()) }
    single<OrderUseCases> { OrderUseCases(get()) }

}
