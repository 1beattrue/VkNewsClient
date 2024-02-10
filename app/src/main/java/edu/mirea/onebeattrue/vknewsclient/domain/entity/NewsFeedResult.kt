package edu.mirea.onebeattrue.vknewsclient.domain.entity

sealed class NewsFeedResult {
    object Error : NewsFeedResult()
    data class Success(val posts: List<FeedPost>) : NewsFeedResult()
}
