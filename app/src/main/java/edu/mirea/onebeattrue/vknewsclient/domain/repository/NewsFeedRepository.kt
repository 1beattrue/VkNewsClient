package edu.mirea.onebeattrue.vknewsclient.domain.repository

import edu.mirea.onebeattrue.vknewsclient.domain.entity.AuthState
import edu.mirea.onebeattrue.vknewsclient.domain.entity.FeedPost
import edu.mirea.onebeattrue.vknewsclient.domain.entity.PostComment
import kotlinx.coroutines.flow.StateFlow

interface NewsFeedRepository {
    fun getAuthStateFlow(): StateFlow<AuthState>
    fun getRecommendations(): StateFlow<List<FeedPost>>
    suspend fun checkAuthState()
    suspend fun loadNextData()
    suspend fun changeLikeStatus(feedPost: FeedPost)
    suspend fun deletePost(feedPost: FeedPost)
    fun getComments(
        feedPost: FeedPost, oldComments: List<PostComment> = listOf()
    ): StateFlow<List<PostComment>>
}