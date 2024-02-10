package edu.mirea.onebeattrue.vknewsclient.presentation

import android.app.Application
import edu.mirea.onebeattrue.vknewsclient.di.ApplicationComponent
import edu.mirea.onebeattrue.vknewsclient.di.DaggerApplicationComponent
import edu.mirea.onebeattrue.vknewsclient.domain.entity.FeedPost
import edu.mirea.onebeattrue.vknewsclient.domain.entity.StatisticItem

class VkNewsApplication : Application() {
    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(
            this, FeedPost( // TODO: временное решение
                1L,
                1L,
                "",
                "",
                "",
                "",
                "",
                listOf<StatisticItem>(),
                false
            )
        )
    }
}