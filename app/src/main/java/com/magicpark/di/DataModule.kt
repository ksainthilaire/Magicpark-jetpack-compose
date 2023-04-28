package com.magicpark.core.di

import android.content.res.Resources
import androidx.room.Room
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import com.magicpark.core.Config
import com.magicpark.data.api.MagicparkApi
import com.magicpark.data.local.AppDatabase
import com.magicpark.data.repositories.MovieRepository
import com.magicpark.data.session.MovieDbSession
import com.magicpark.domain.repositories.IMovieRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val dataModule = module {

    single<Resources> {
        androidContext().resources
    }

    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java, "database-magicpark"
        ).build()
    }

    single {
        val database = get<AppDatabase>()
        database.movieDao()
    }

    single<IMovieRepository> {
        MovieRepository()
    }


    single {

        val logging = HttpLoggingInterceptor();
      //  logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient().newBuilder().addInterceptor(logging).callTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }



    single {
        val httpClient = get<OkHttpClient>()


        Retrofit.Builder()
            .baseUrl(Config.apiUrl)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    single<MagicparkApi> {
        get<Retrofit>().create(MagicparkApi::class.java)
    }

    single<MovieDbSession> {
        MovieDbSession(androidContext())
    }
}