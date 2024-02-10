package edu.mirea.onebeattrue.vknewsclient.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.mirea.onebeattrue.vknewsclient.domain.entity.FeedPost
import edu.mirea.onebeattrue.vknewsclient.domain.entity.PostComment
import edu.mirea.onebeattrue.vknewsclient.domain.usecase.GetCommentsUseCase
import edu.mirea.onebeattrue.vknewsclient.extensions.mergeWith
import edu.mirea.onebeattrue.vknewsclient.presentation.states.CommentsScreenState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class CommentsViewModel @Inject constructor(
    private val feedPost: FeedPost,
    private val getCommentsUseCase: GetCommentsUseCase
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        Log.d("NewsFeedViewModel", "Exception caught by Exception Handler")
    }

    private val comments = getCommentsUseCase(feedPost)

    private val loadNextDataFlow = MutableSharedFlow<CommentsScreenState>()

    val screenState = comments
        .map { CommentsScreenState.Comments(feedPost, it) as CommentsScreenState }
        .onStart { emit(CommentsScreenState.Loading) }
        .mergeWith(loadNextDataFlow)

    private var oldComments: List<PostComment> = listOf()

    fun loadComments(feedPost: FeedPost) {
        viewModelScope.launch(exceptionHandler) {
            loadNextDataFlow.emit(
                CommentsScreenState.Comments(
                    feedPost = feedPost,
                    comments = comments.value
                )
            )
            oldComments = comments.value
        }
    }
}