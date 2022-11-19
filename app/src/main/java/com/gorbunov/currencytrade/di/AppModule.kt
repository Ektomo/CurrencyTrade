package com.gorbunov.currencytrade.di

import android.content.Context
import com.gorbunov.currencytrade.CurrencyDataStore
import com.gorbunov.currencytrade.gate.Connection
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDataStoreRepository(
        @ApplicationContext app: Context
    ): CurrencyDataStore = CurrencyDataStore(app)

    @Singleton
    @Provides
    fun provideGate(): Connection = Connection()

}