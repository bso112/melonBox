package com.seoulventure.melonbox.di

import com.seoulventure.melonbox.data.KtorClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object KtorModule {

    @Provides
    @Singleton
    fun provideKtorHttpClient(): HttpClient = KtorClient.httpClient

}