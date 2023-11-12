package edu.mirea.onebeattrue.vknewsclient.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.mirea.onebeattrue.vknewsclient.domain.FeedPost
import edu.mirea.onebeattrue.vknewsclient.domain.StatisticItem
import edu.mirea.onebeattrue.vknewsclient.presentation.states.NewsFeedScreenState

class NewsFeedViewModel : ViewModel() {
    // initialization ******************************************************************************
    private val sourcePosts = mutableListOf<FeedPost>().apply {
        repeat(10) {
            add(FeedPost(id = it))
        }
    }
    private val initialState = NewsFeedScreenState.Posts(posts = sourcePosts)
    // *********************************************************************************************

    private val _screenState = MutableLiveData<NewsFeedScreenState>(initialState)
    val screenState: LiveData<NewsFeedScreenState>
        get() = _screenState

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

    fun remove(feedPost: FeedPost) {
        val currentState = _screenState.value
        if (currentState !is NewsFeedScreenState.Posts) return

        val feedPosts = currentState.posts.toMutableList()
        feedPosts.remove(feedPost)
        _screenState.value = NewsFeedScreenState.Posts(posts = feedPosts)
    }
}