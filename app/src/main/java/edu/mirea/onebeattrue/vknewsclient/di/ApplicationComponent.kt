package edu.mirea.onebeattrue.vknewsclient.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import edu.mirea.onebeattrue.vknewsclient.domain.entity.FeedPost
import edu.mirea.onebeattrue.vknewsclient.presentation.MainActivity

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class
    ]
)
interface ApplicationComponent {

    fun inject(mainActivity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            @BindsInstance feedPost: FeedPost
        ): ApplicationComponent
    }
}