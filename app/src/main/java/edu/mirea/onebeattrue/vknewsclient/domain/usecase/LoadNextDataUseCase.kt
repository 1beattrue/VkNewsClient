package edu.mirea.onebeattrue.vknewsclient.domain.usecase

import edu.mirea.onebeattrue.vknewsclient.domain.repository.NewsFeedRepository

class LoadNextDataUseCase(private val repository: NewsFeedRepository) {
    suspend operator fun invoke() {
        repository.loadNextData()
    }
}