package io.dvlt.core.di

import android.content.res.Resources
import androidx.room.Room
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.dvlt.core.Config
import io.dvlt.data.api.MovieDbApi
import io.dvlt.data.local.AppDatabase
import io.dvlt.data.repositories.MovieRepository
import io.dvlt.data.session.MovieDbSession
import io.dvlt.domain.repositories.IMovieRepository
import io.dvlt.themoviedbtest.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import java.util.logging.Level

val dataModule = module {

    single<Resources> {
        androidContext().resources
    }

    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java, "database-devialet"
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

    single<MovieDbApi> {
        get<Retrofit>().create(MovieDbApi::class.java)
    }

    single<MovieDbSession> {
        MovieDbSession(androidContext())
    }
}