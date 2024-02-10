package edu.mirea.onebeattrue.vknewsclient.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.mirea.onebeattrue.vknewsclient.domain.entity.FeedPost

class CommentsViewModelFactory(
    private val feedPost: FeedPost,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CommentsViewModel(
            feedPost = feedPost,
            application = application
        ) as T
    }
}