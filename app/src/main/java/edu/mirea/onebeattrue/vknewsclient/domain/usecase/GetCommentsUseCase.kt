package edu.mirea.onebeattrue.vknewsclient.domain.usecase

import edu.mirea.onebeattrue.vknewsclient.domain.entity.FeedPost
import edu.mirea.onebeattrue.vknewsclient.domain.entity.PostComment
import edu.mirea.onebeattrue.vknewsclient.domain.repository.NewsFeedRepository
import kotlinx.coroutines.flow.StateFlow

class GetCommentsUseCase(private val repository: NewsFeedRepository) {
    operator fun invoke(
        feedPost: FeedPost,
        oldComments: List<PostComment> = listOf()
    ): StateFlow<List<PostComment>> {
        return repository.getComments(feedPost, oldComments)
    }
}