package edu.mirea.onebeattrue.vknewsclient.di

import android.content.Context
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import dagger.Binds
import dagger.Module
import dagger.Provides
import edu.mirea.onebeattrue.vknewsclient.data.network.ApiFactory
import edu.mirea.onebeattrue.vknewsclient.data.network.ApiService
import edu.mirea.onebeattrue.vknewsclient.data.repository.NewsFeedRepositoryImpl
import edu.mirea.onebeattrue.vknewsclient.domain.repository.NewsFeedRepository

@Module
interface DataModule {
    @ApplicationScope
    @Binds
    fun bindRepository(impl: NewsFeedRepositoryImpl): NewsFeedRepository

    companion object {
        @ApplicationScope
        @Provides
        fun provideApiService(): ApiService = ApiFactory.apiService

        @ApplicationScope
        @Provides
        fun provideVkStorage(
            context: Context
        ): VKPreferencesKeyValueStorage = VKPreferencesKeyValueStorage(context)
    }
}