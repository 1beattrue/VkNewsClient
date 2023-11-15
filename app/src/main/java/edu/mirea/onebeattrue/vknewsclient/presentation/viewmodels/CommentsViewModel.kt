package edu.mirea.onebeattrue.vknewsclient.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.mirea.onebeattrue.vknewsclient.data.repository.NewsFeedRepository
import edu.mirea.onebeattrue.vknewsclient.domain.FeedPost
import edu.mirea.onebeattrue.vknewsclient.domain.PostComment
import edu.mirea.onebeattrue.vknewsclient.presentation.states.CommentsScreenState
import kotlinx.coroutines.launch

class CommentsViewModel(
    feedPost: FeedPost,
    application: Application
) : ViewModel() {

    private val repository = NewsFeedRepository(application)

    private val _screenState = MutableLiveData<CommentsScreenState>(CommentsScreenState.Initial)
    val screenState: LiveData<CommentsScreenState>
        get() = _screenState

    private var oldComments: List<PostComment> = listOf()

    init {
        _screenState.value = CommentsScreenState.Loading
        loadComments(feedPost)
    }

    fun loadComments(feedPost: FeedPost) {
        viewModelScope.launch {
            val comments = repository.getComments(
                feedPost = feedPost,
                oldComments = oldComments,
            )
            _screenState.value = CommentsScreenState.Comments(
                feedPost = feedPost,
                comments = comments
            )
            oldComments = comments
        }
    }
}