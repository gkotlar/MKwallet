package com.example.mkwallet.network

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import com.example.mkwallet.data.MyRoomDB
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(ExchangeRatesApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    @Provides
    @Singleton
    fun provideExchangeRatesListAPI(retrofit: Retrofit): ExchangeRatesApi =
        retrofit.create(ExchangeRatesApi::class.java)


    @Provides
    @Singleton
    fun provideDatabase(app: Application): MyRoomDB =
        Room.databaseBuilder(app, MyRoomDB::class.java, "mk_wallet_database")
            .build()
}