package edu.mirea.onebeattrue.vknewsclient.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import edu.mirea.onebeattrue.vknewsclient.data.repository.NewsFeedRepository
import edu.mirea.onebeattrue.vknewsclient.domain.FeedPost
import edu.mirea.onebeattrue.vknewsclient.domain.StatisticItem
import edu.mirea.onebeattrue.vknewsclient.presentation.states.NewsFeedScreenState
import kotlinx.coroutines.launch

class NewsFeedViewModel(application: Application) : AndroidViewModel(application) {
    // initialization ******************************************************************************
    private val initialState = NewsFeedScreenState.Initial
    // *********************************************************************************************

    private val _screenState = MutableLiveData<NewsFeedScreenState>(initialState)
    val screenState: LiveData<NewsFeedScreenState>
        get() = _screenState

    private val repository = NewsFeedRepository(application)

    init {
        _screenState.value = NewsFeedScreenState.Loading
        loadRecommendations()
    }

    fun changeLikeStatus(feedPost: FeedPost) {
        viewModelScope.launch {
            repository.changeLikeStatus(feedPost)
            _screenState.value = NewsFeedScreenState.Posts(repository.feedPosts)
        }
    }

    private fun loadRecommendations() {
        viewModelScope.launch {
            val feedPosts = repository.loadRecommendations()
            _screenState.value = NewsFeedScreenState.Posts(feedPosts)
        }
    }

    fun loadNextRecommendations() {
        _screenState.value = NewsFeedScreenState.Posts(
            repository.feedPosts,
            true
        )
        loadRecommendations()
    }

    fun deletePost(feedPost: FeedPost) {
        viewModelScope.launch {
            repository.deletePost(feedPost)
            _screenState.value = NewsFeedScreenState.Posts(repository.feedPosts)
        }
    }

    fun updateCount(oldFeedPost: FeedPost, statisticItem: StatisticItem) {
        val currentState = _screenState.value
        if (currentState !is NewsFeedScreenState.Posts) return

        val oldStatistics = oldFeedPost.statistics
        val newStatistics = oldStatistics.toMutableList().apply {
            replaceAll { oldItem ->
                if (oldItem.type == statisticItem.type) {
                    oldItem.copy(count = oldItem.count + 1)
                } else {
                    oldItem
                }
            }
        }
        val newFeedPost = oldFeedPost.copy(statistics = newStatistics)

        val oldFeedPosts = currentState.posts.toMutableList()
        val newFeedPosts = oldFeedPosts.apply {
            replaceAll { oldItem ->
                if (oldItem.id == oldFeedPost.id) {
                    newFeedPost
                } else {
                    oldItem
                }
            }
        }

        _screenState.value = NewsFeedScreenState.Posts(posts = newFeedPosts)
    }
}