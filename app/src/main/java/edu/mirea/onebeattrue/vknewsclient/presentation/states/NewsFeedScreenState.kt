package edu.mirea.onebeattrue.vknewsclient.presentation.states

import edu.mirea.onebeattrue.vknewsclient.domain.entity.FeedPost

sealed class NewsFeedScreenState {
    object Initial : NewsFeedScreenState()

    object Loading : NewsFeedScreenState()

    data class Posts(
        val posts: List<FeedPost>,
        val nextDataIsLoading: Boolean = false
    ) : NewsFeedScreenState()
}
