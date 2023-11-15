package edu.mirea.onebeattrue.vknewsclient.data.repository

import android.app.Application
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import edu.mirea.onebeattrue.vknewsclient.data.mapper.NewsFeedMapper
import edu.mirea.onebeattrue.vknewsclient.data.network.ApiFactory
import edu.mirea.onebeattrue.vknewsclient.domain.FeedPost
import edu.mirea.onebeattrue.vknewsclient.domain.PostComment
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

    private var nextFrom: String? = null

    suspend fun loadRecommendations(): List<FeedPost> {
        val startFrom = nextFrom

        if (startFrom == null && feedPosts.isNotEmpty()) return feedPosts // на случай, если рекомендаций больше нет

        val response = if (startFrom == null) {
            apiService.loadRecommendations(getAccessToken())
        } else {
            apiService.loadRecommendations(getAccessToken(), startFrom)
        }
        nextFrom = response.content.nextFrom
        val posts = mapper.mapResponseToPosts(responseDto = response)
        _feedPosts.addAll(posts)
        return feedPosts
    }

    suspend fun changeLikeStatus(feedPost: FeedPost) {
        val response = if (feedPost.isLiked) {
            apiService.deleteLike(
                token = getAccessToken(),
                ownerId = feedPost.communityId,
                postId = feedPost.id
            )
        } else {
            apiService.addLike(
                token = getAccessToken(),
                ownerId = feedPost.communityId,
                postId = feedPost.id
            )
        }

        val newLikesCount = response.likes.count
        val newStatistics = feedPost.statistics.toMutableList().apply {
            removeIf { it.type == StatisticType.LIKES }
            add(StatisticItem(type = StatisticType.LIKES, count = newLikesCount))
        }

        val newPost = feedPost.copy(isLiked = !feedPost.isLiked, statistics = newStatistics)
        val postIndex = _feedPosts.indexOf(feedPost)
        _feedPosts[postIndex] = newPost
    }

    suspend fun deletePost(feedPost: FeedPost) {
        apiService.ignorePost(
            token = getAccessToken(),
            ownerId = feedPost.communityId,
            postId = feedPost.id
        )
        _feedPosts.remove(feedPost)
    }

    suspend fun getComments(
        feedPost: FeedPost,
        oldComments: List<PostComment> = listOf()
    ): List<PostComment> {
        if (oldComments.isEmpty()) {
            val response = apiService.getComments(
                token = getAccessToken(),
                ownerId = feedPost.communityId,
                postId = feedPost.id
            )
            return mapper.mapResponseToComments(response)
        }
        val startComment = oldComments.last()
        val response = apiService.getComments(
            token = getAccessToken(),
            ownerId = feedPost.communityId,
            postId = feedPost.id,
            startCommentId = startComment.id
        )
        val newComments = mapper.mapResponseToComments(response).filter { it != startComment }
        return oldComments + newComments
    }

    private fun getAccessToken(): String {
        return token?.accessToken ?: throw RuntimeException("token is null")
    }
}