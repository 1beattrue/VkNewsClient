package edu.mirea.onebeattrue.vknewsclient.data.repository

import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import edu.mirea.onebeattrue.vknewsclient.data.mapper.NewsFeedMapper
import edu.mirea.onebeattrue.vknewsclient.data.network.ApiService
import edu.mirea.onebeattrue.vknewsclient.domain.entity.AuthState
import edu.mirea.onebeattrue.vknewsclient.domain.entity.FeedPost
import edu.mirea.onebeattrue.vknewsclient.domain.entity.PostComment
import edu.mirea.onebeattrue.vknewsclient.domain.entity.StatisticItem
import edu.mirea.onebeattrue.vknewsclient.domain.entity.StatisticType
import edu.mirea.onebeattrue.vknewsclient.domain.repository.NewsFeedRepository
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
import javax.inject.Inject

class NewsFeedRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val mapper: NewsFeedMapper,
    private val storage: VKPreferencesKeyValueStorage,
) : NewsFeedRepository {
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

    private val checkAuthStateEvents = MutableSharedFlow<Unit>(replay = 1)

    private val authStateFlow = flow {
        checkAuthStateEvents.emit(Unit)
        checkAuthStateEvents.collect {
            val currentToken = token
            val loggedIn = currentToken != null && currentToken.isValid
            val authState = if (loggedIn) AuthState.Authorized else AuthState.NotAuthorized
            emit(authState)
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = AuthState.Initial
    )

    private val token
        get() = VKAccessToken.restore(storage)

    private val _feedPosts = mutableListOf<FeedPost>()
    private val feedPosts: List<FeedPost>
        get() = _feedPosts.toList()

    private var nextFrom: String? = null

    private val recommendations: StateFlow<List<FeedPost>> = loadedListFlow
        .mergeWith(refreshedListFlow)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = feedPosts
        )

    override fun getAuthStateFlow(): StateFlow<AuthState> = authStateFlow

    override fun getRecommendations(): StateFlow<List<FeedPost>> = recommendations

    override suspend fun checkAuthState() {
        checkAuthStateEvents.emit(Unit)
    }

    override suspend fun loadNextData() {
        nextDataNeededEvents.emit(Unit)
    }

    override suspend fun changeLikeStatus(feedPost: FeedPost) {
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

    override suspend fun deletePost(feedPost: FeedPost) {
        apiService.ignorePost(
            token = getAccessToken(),
            ownerId = feedPost.communityId,
            postId = feedPost.id
        )
        _feedPosts.remove(feedPost)

        refreshedListFlow.emit(feedPosts)
    }

    override fun getComments(
        feedPost: FeedPost,
        oldComments: List<PostComment>
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
            initialValue = listOf()
        )

    private fun getAccessToken(): String {
        return token?.accessToken ?: throw RuntimeException("token is null")
    }

    companion object {
        private const val RETRY_TIMEOUT_MILLIS = 3000L
    }
}