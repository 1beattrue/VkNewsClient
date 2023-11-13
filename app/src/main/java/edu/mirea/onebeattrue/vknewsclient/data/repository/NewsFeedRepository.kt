package edu.mirea.onebeattrue.vknewsclient.data.repository

import android.app.Application
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import edu.mirea.onebeattrue.vknewsclient.data.mapper.NewsFeedMapper
import edu.mirea.onebeattrue.vknewsclient.data.network.ApiFactory
import edu.mirea.onebeattrue.vknewsclient.domain.FeedPost
import edu.mirea.onebeattrue.vknewsclient.domain.StatisticItem
import edu.mirea.onebeattrue.vknewsclient.domain.StatisticType

class NewsFeedRepository(
    application: Application
) {
    private val storage = VKPreferencesKeyValueStorage(application)
    private val token = VKAccessToken.restore(storage)

    private val apiService = ApiFactory.apiService
    private val mapper = NewsFeedMapper()

    private val _feedPosts = mutableListOf<FeedPost>()
    val feedPosts: List<FeedPost>
        get() = _feedPosts.toList()

    suspend fun loadRecommendations(): List<FeedPost> {
        val response = apiService.loadRecommendations(getAccessToken())
        val posts = mapper.mapResponseToPosts(responseDto = response)
        _feedPosts.addAll(posts)
        return posts
    }

    suspend fun addLike(feedPost: FeedPost) {
        val response = apiService.addLike(
            token = getAccessToken(),
            ownerId = feedPost.communityId,
            postId = feedPost.id
        )

        val newLikesCount = response.likes.count
        val newStatistics = feedPost.statistics.toMutableList().apply {
            removeIf { it.type == StatisticType.LIKES }
            add(StatisticItem(type = StatisticType.LIKES, count = newLikesCount))
        }

        val newPost = feedPost.copy(isLiked = true, statistics = newStatistics)
        val postIndex = _feedPosts.indexOf(feedPost)
        _feedPosts[postIndex] = newPost
    }

    suspend fun deleteLike(feedPost: FeedPost) {
        val response = apiService.deleteLike(
            token = getAccessToken(),
            ownerId = feedPost.communityId,
            postId = feedPost.id
        )

        val newLikesCount = response.likes.count
        val newStatistics = feedPost.statistics.toMutableList().apply {
            removeIf { it.type == StatisticType.LIKES }
            add(StatisticItem(type = StatisticType.LIKES, count = newLikesCount))
        }

        val newPost = feedPost.copy(isLiked = false, statistics = newStatistics)
        val postIndex = _feedPosts.indexOf(feedPost)
        _feedPosts[postIndex] = newPost
    }

    private fun getAccessToken(): String {
        return token?.accessToken ?: throw RuntimeException("token is null")
    }
}