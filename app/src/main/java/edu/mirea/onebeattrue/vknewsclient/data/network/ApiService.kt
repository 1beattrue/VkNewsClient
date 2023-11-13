package edu.mirea.onebeattrue.vknewsclient.data.network

import edu.mirea.onebeattrue.vknewsclient.data.model.LikesCountResponseDto
import edu.mirea.onebeattrue.vknewsclient.data.model.NewsFeedResponseDto
import edu.mirea.onebeattrue.vknewsclient.data.model.PostDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("newsfeed.getRecommended?v=5.154")
    suspend fun loadRecommendations(
        @Query("access_token") token: String
    ): NewsFeedResponseDto

    @GET("likes.add?v=5.154&type=post")
    suspend fun addLike(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("item_id") postId: Long
    ): LikesCountResponseDto

    @GET("likes.delete?v=5.154&type=post")
    suspend fun deleteLike(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("item_id") postId: Long
    ): LikesCountResponseDto
}