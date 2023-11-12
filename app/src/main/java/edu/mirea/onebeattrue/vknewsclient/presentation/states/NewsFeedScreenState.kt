package edu.mirea.onebeattrue.vknewsclient.presentation.states

import edu.mirea.onebeattrue.vknewsclient.domain.FeedPost

sealed class NewsFeedScreenState {
    object Initial : NewsFeedScreenState()
    data class Posts(val posts: List<FeedPost>) : NewsFeedScreenState()
}
