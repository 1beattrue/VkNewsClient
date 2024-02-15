package edu.mirea.onebeattrue.vknewsclient.di

import dagger.BindsInstance
import dagger.Subcomponent
import edu.mirea.onebeattrue.vknewsclient.domain.entity.FeedPost
import edu.mirea.onebeattrue.vknewsclient.presentation.viewmodels.ViewModelFactory

@Subcomponent(
    modules = [CommentViewModelModule::class]
)
interface CommentScreenComponent {

    fun getViewModelFactory(): ViewModelFactory

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance feedPost: FeedPost
        ): CommentScreenComponent
    }
}