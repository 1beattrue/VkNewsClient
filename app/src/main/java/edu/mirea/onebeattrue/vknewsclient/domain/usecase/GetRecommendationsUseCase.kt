package edu.mirea.onebeattrue.vknewsclient.domain.usecase

import edu.mirea.onebeattrue.vknewsclient.domain.entity.FeedPost
import edu.mirea.onebeattrue.vknewsclient.domain.repository.NewsFeedRepository
import kotlinx.coroutines.flow.StateFlow

class GetRecommendationsUseCase(private val repository: NewsFeedRepository) {
    operator fun invoke(): StateFlow<List<FeedPost>> {
        return repository.getRecommendations()
    }
}