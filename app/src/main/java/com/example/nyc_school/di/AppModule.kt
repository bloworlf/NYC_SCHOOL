package com.example.nyc_school.di

import android.content.Context
import androidx.room.Room
import com.example.nyc_school.data.database.AppDatabase
import com.example.nyc_school.data.impl.RepositoryImpl
import com.example.nyc_school.data.network.ApiCall
import com.example.nyc_school.data.repo.Repository
import com.example.nyc_school.utils.Common
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    //<editor-fold desc="Network">
    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Common.API.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiCall(
        retrofit: Retrofit
    ): ApiCall {
        return retrofit.create(ApiCall::class.java)
    }

    //</editor-fold>

    //<editor-fold desc="Local">
    @Singleton
    @Provides
    fun providesAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "nyc_db"
        ).build()
    }
    //</editor-fold>

    @Provides
    @Singleton
    fun provideRepository(
        apiCall: ApiCall,
    ): Repository {
        return RepositoryImpl(
            apiCall,
        )
    }
}