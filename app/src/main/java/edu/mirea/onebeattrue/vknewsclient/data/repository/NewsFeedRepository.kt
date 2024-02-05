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
import edu.mirea.onebeattrue.vknewsclient.extensions.mergeWith
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn

class NewsFeedRepository(
    application: Application
) {
    private val refreshedListFlow = MutableSharedFlow<List<FeedPost>>()
    private val nextDataNeededEvents = MutableSharedFlow<Unit>(replay = 1)
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val loadedListFlow = flow {
        nextDataNeededEvents.emit(Unit)
        nextDataNeededEvents.collect {
            val startFrom = nextFrom

            if (startFrom == null && feedPosts.isNotEmpty()) {
                emit(feedPosts) // на случай, если рекомендаций больше нет
                return@collect
            }

            val response = if (startFrom == null) {
                apiService.loadRecommendations(getAccessToken())
            } else {
                apiService.loadRecommendations(getAccessToken(), startFrom)
            }
            nextFrom = response.content.nextFrom
            val posts = mapper.mapResponseToPosts(responseDto = response)
            _feedPosts.addAll(posts)
            emit(feedPosts)
        }
    }
//        .map { NewsFeedResult.Success(it) as NewsFeedResult } // обработка ошибок
        .retry {
            delay(RETRY_TIMEOUT_MILLIS)
            true
        }
    // .catch { emit(NewsFeedResult.Error) }

    private val storage = VKPreferencesKeyValueStorage(application)
    private val token = VKAccessToken.restore(storage)

    private val apiService = ApiFactory.apiService
    private val mapper = NewsFeedMapper()

    private val _feedPosts = mutableListOf<FeedPost>()
    private val feedPosts: List<FeedPost>
        get() = _feedPosts.toList()

    private var nextFrom: String? = null

    val recommendations: StateFlow<List<FeedPost>> = loadedListFlow
        .mergeWith(refreshedListFlow)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = feedPosts
        )

    suspend fun loadNextData() {
        nextDataNeededEvents.emit(Unit)
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

        refreshedListFlow.emit(feedPosts)
    }

    suspend fun deletePost(feedPost: FeedPost) {
        apiService.ignorePost(
            token = getAccessToken(),
            ownerId = feedPost.communityId,
            postId = feedPost.id
        )
        _feedPosts.remove(feedPost)

        refreshedListFlow.emit(feedPosts)
    }

    fun getComments(
        feedPost: FeedPost,
        oldComments: List<PostComment> = listOf()
    ): StateFlow<List<PostComment>> = flow {
        if (oldComments.isEmpty()) {
            val response = apiService.getComments(
                token = getAccessToken(),
                ownerId = feedPost.communityId,
                postId = feedPost.id
            )
            emit(mapper.mapResponseToComments(response))
        }
        val startComment = oldComments.last()
        val response = apiService.getComments(
            token = getAccessToken(),
            ownerId = feedPost.communityId,
            postId = feedPost.id,
            startCommentId = startComment.id
        )
        val newComments = mapper.mapResponseToComments(response).filter { it != startComment }
        emit(oldComments + newComments)
    }
        .retry {
            delay(RETRY_TIMEOUT_MILLIS)
            true
        }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = oldComments
        )

    private fun getAccessToken(): String {
        return token?.accessToken ?: throw RuntimeException("token is null")
    }

    companion object {
        private const val RETRY_TIMEOUT_MILLIS = 3000L
    }
}