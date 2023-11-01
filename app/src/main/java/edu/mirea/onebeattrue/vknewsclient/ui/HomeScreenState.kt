package edu.mirea.onebeattrue.vknewsclient.ui

import edu.mirea.onebeattrue.vknewsclient.domain.FeedPost
import edu.mirea.onebeattrue.vknewsclient.domain.PostComment

sealed class HomeScreenState {
    object Initial : HomeScreenState()
    data class Posts(val posts: List<FeedPost>) : HomeScreenState()
    data class Comments(val feedPost: FeedPost, val comments: List<PostComment>) : HomeScreenState()
}
