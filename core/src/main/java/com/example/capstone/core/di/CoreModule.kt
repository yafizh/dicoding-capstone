package com.example.capstone.core.di

import androidx.room.Room
import com.example.capstone.core.BuildConfig
import com.example.capstone.core.data.MovieRepository
import com.example.capstone.core.data.SettingRepository
import com.example.capstone.core.data.source.local.LocalDataSource
import com.example.capstone.core.data.source.local.preferences.SettingPreferences
import com.example.capstone.core.data.source.local.preferences.settingDataStore
import com.example.capstone.core.data.source.local.room.MovieDatabase
import com.example.capstone.core.data.source.remote.RemoteDataSource
import com.example.capstone.core.data.source.remote.network.ApiService
import com.example.capstone.core.domain.repository.IMovieRepository
import com.example.capstone.core.domain.repository.ISettingRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val databaseModule = module {
    single {
        Room.databaseBuilder(androidContext(), MovieDatabase::class.java, "Movie.db")
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }
    factory { get<MovieDatabase>().movieDao() }
    single { SettingPreferences(androidContext().settingDataStore) }
}

val networkModule = module {
    single {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                val url = request.url.newBuilder()
                    .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                    .build()
                chain.proceed(request.newBuilder().url(url).build())
            }
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
            .create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    single<IMovieRepository> { MovieRepository(get(), get()) }
    single<ISettingRepository> { SettingRepository(get()) }
}
