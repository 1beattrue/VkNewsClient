package edu.mirea.onebeattrue.vknewsclient.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.mirea.onebeattrue.vknewsclient.domain.FeedPost
import edu.mirea.onebeattrue.vknewsclient.domain.PostComment
import edu.mirea.onebeattrue.vknewsclient.ui.states.CommentsScreenState

class CommentsViewModel(
    feedPost: FeedPost
) : ViewModel() {
    // initialization ******************************************************************************
    private val sourceComments = mutableListOf<PostComment>().apply {
        repeat(10) {
            add(PostComment(id = it))
        }
    }
    private val initialState = CommentsScreenState.Initial
    // *********************************************************************************************

    private val _screenState = MutableLiveData<CommentsScreenState>(initialState)
    val screenState: LiveData<CommentsScreenState>
        get() = _screenState

    init {
        loadComments(feedPost)
    }

    private fun loadComments(feedPost: FeedPost) { // временный метод для имитации загрузки из сети
        _screenState.value = CommentsScreenState.Comments(
            feedPost = feedPost,
            comments = sourceComments
        )
    }
}