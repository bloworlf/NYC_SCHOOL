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
    fun provideOkHttp(/*cache: Cache, deviceId: String*/): OkHttpClient {
        return OkHttpClient.Builder()
//            .cache(cache)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
//            .certificatePinner(CertificatePinner.Builder().add("","").build())
//            .addInterceptor { chain ->
//                // Get the request from the chain.
//                var request = chain.request()
//
//                request = if (App.instance.hasNetwork()!!) {
//                    /*
//                        *  If there is Internet, get the cache that was stored 5 seconds ago.
//                        *  If the cache is older than 5 seconds, then discard it,
//                        *  and indicate an error in fetching the response.
//                        *  The 'max-age' attribute is responsible for this behavior.
//                        */
//                    request.newBuilder().header("Cache-Control", "public, max-age=" + 5)
//                } else {
//                    /*
//                        *  If there is no Internet, get the cache that was stored 7 days ago.
//                        *  If the cache is older than 7 days, then discard it,
//                        *  and indicate an error in fetching the response.
//                        *  The 'max-stale' attribute is responsible for this behavior.
//                        *  The 'only-if-cached' attribute indicates to not retrieve new data; fetch the cache only instead.
//                        */
//                    request.newBuilder().header(
//                        "Cache-Control",
//                        "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
//                    )
//                }
//                    .header("x-device-id", deviceId)
//                    .header("x-api-key", "")
//                    .header("x-session-key", "")
//                    .header("x-user-agent", "")
//                    .method(request.method, request.body)
//                    .build()
//
//                // Add the modified request to the chain.
//                chain.proceed(request)
//            }
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