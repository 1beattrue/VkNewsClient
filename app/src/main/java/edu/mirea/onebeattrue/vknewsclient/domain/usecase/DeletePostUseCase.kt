package edu.mirea.onebeattrue.vknewsclient.domain.usecase

import edu.mirea.onebeattrue.vknewsclient.domain.entity.FeedPost
import edu.mirea.onebeattrue.vknewsclient.domain.repository.NewsFeedRepository

class DeletePostUseCase(private val repository: NewsFeedRepository) {
    suspend operator fun invoke(feedPost: FeedPost) {
        repository.deletePost(feedPost)
    }
}