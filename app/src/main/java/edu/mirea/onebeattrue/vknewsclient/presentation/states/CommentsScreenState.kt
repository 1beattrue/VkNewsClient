package edu.mirea.onebeattrue.vknewsclient.presentation.states

import edu.mirea.onebeattrue.vknewsclient.domain.entity.FeedPost
import edu.mirea.onebeattrue.vknewsclient.domain.entity.PostComment

sealed class CommentsScreenState {
    object Initial : CommentsScreenState()
    object Loading : CommentsScreenState()
    data class Comments(
        val feedPost: FeedPost,
        val comments: List<PostComment>
    ): CommentsScreenState()
}
