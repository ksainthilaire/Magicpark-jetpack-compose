package com.magicpark.di

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.room.Room
import com.magicpark.core.Config
import com.magicpark.core.Config.KEY_SHARED_PREFERENCES
import com.magicpark.data.api.MagicparkApi
import com.magicpark.data.local.AppDatabase
import com.magicpark.data.repositories.*
import com.magicpark.domain.repositories.*
import com.magicpark.domain.usecases.*
import com.magicpark.features.login.forgot.ForgotViewModel
import com.magicpark.features.login.login.LoginFragmentViewModel
import com.magicpark.features.login.register.RegisterViewModel
import settings.SettingsViewModel
import com.magicpark.features.shop.shopList.ShopListViewModel
import com.magicpark.features.shop.shopItem.ShopItemViewModel
import wallet.WalletViewModel
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import  com.magicpark.features.shop.cart.CartViewModel
import ticket.TicketViewModel
import com.magicpark.features.payment.payment.PaymentViewModel
import com.magicpark.features.account.AccountViewModel
import com.magicpark.features.login.LoginActivityViewModel
import com.magicpark.features.payment.invoice.PaymentInvoiceViewModel
import com.magicpark.features.payment.invoice.PaymentInvoiceListViewModel
import com.magicpark.features.shop.Cart
import com.magicpark.ui.splash.SplashViewModel
import com.magicpark.utils.ui.Session
import contact.ContactViewModel

val AppModule = module {

    single<Resources> {
        androidContext().resources
    }

    viewModel { LoginFragmentViewModel() }
    viewModel { LoginActivityViewModel() }
    viewModel { RegisterViewModel() }
    viewModel { ForgotViewModel() }
    viewModel { ContactViewModel() }

    viewModel { ShopListViewModel() }
    viewModel { ShopItemViewModel(get()) }
    viewModel { WalletViewModel() }
    viewModel { TicketViewModel() }
    viewModel { SettingsViewModel() }

    viewModel { CartViewModel() }
    viewModel { AccountViewModel() }
    viewModel { PaymentViewModel(get()) }
    viewModel { PaymentInvoiceViewModel(get()) }
    viewModel { PaymentInvoiceListViewModel() }
    viewModel { SplashViewModel() }

    single {
       Session(androidContext())
    }

    single {
        Cart(androidContext())
    }

    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java, "database-magicpark"
        ).build()
    }

    single {
        val context = androidContext()

        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(
                KEY_SHARED_PREFERENCES,
                Context.MODE_PRIVATE)


        val tokenInterceptor = Interceptor { chain ->
            val token = sharedPreferences
                .getString("KEY-API-TOKEN", null) ?: ""

            val request = chain.request().newBuilder()
                .addHeader("token", token)
                .build()

            chain.proceed(request)
        }

        val logging = HttpLoggingInterceptor()

        OkHttpClient()
            .newBuilder()
            .addInterceptor(tokenInterceptor)
            .addInterceptor(logging)
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


    single {
        val database = get<AppDatabase>()
        database.shopDao()
    }

    single {
        val database = get<AppDatabase>()
        database.ticketDao()
    }


    single<IShopRepository> { ShopRepository() }
    single<IUserRepository> { UserRepository() }
    single<ISupportRepository> { SupportRepository() }
    single<ITicketRepository> { TicketRepository() }
    single<ISettingsRepository> { SettingsRepository(get()) }
    single<IOrderRepository> { OrderRepository() }


    single { ShopUseCases(get()) }
    single { UserUseCases(get()) }
    single { SupportUseCases(get()) }
    single { TicketUseCases(get()) }
    single { SettingsUseCases(get()) }
    single { OrderUseCases(get()) }
}
