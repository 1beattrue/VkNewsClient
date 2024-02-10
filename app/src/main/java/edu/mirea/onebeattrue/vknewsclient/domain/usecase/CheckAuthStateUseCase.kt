package edu.mirea.onebeattrue.vknewsclient.domain.usecase

import edu.mirea.onebeattrue.vknewsclient.domain.repository.NewsFeedRepository

class CheckAuthStateUseCase(private val repository: NewsFeedRepository) {
    suspend operator fun invoke() {
        repository.checkAuthState()
    }
}