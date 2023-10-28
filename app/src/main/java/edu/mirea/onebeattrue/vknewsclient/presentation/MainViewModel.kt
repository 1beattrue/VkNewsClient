package edu.mirea.onebeattrue.vknewsclient.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.mirea.onebeattrue.vknewsclient.domain.FeedPost
import edu.mirea.onebeattrue.vknewsclient.domain.StatisticItem

class MainViewModel : ViewModel() {

    private val _feedPosts = MutableLiveData<List<FeedPost>>(listOf())
    val feedPosts: LiveData<List<FeedPost>>
        get() = _feedPosts

    init {
        _feedPosts.value = mutableListOf<FeedPost>().apply {
            repeat(10) {
                add(FeedPost(id = it))
            }
        }
    }

    fun updateCount(oldFeedPost: FeedPost, statisticItem: StatisticItem) {
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

        val oldFeedPosts = _feedPosts.value?.toMutableList() ?: mutableListOf()
        _feedPosts.value = oldFeedPosts.apply {
            replaceAll { oldItem ->
                if (oldItem == oldFeedPost) {
                    newFeedPost
                } else {
                    oldItem
                }
            }
        }
    }

    fun remove(feedPost: FeedPost) {
        val oldFeedPosts = _feedPosts.value?.toMutableList() ?: mutableListOf()
        oldFeedPosts.remove(feedPost)
        _feedPosts.value = oldFeedPosts
    }
}