package edu.mirea.onebeattrue.vknewsclient.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.mirea.onebeattrue.vknewsclient.domain.FeedPost
import edu.mirea.onebeattrue.vknewsclient.domain.PostComment
import edu.mirea.onebeattrue.vknewsclient.domain.StatisticItem
import edu.mirea.onebeattrue.vknewsclient.ui.HomeScreenState

class MainViewModel : ViewModel() {
    // initialization ******************************************************************************
    private val sourceComments = mutableListOf<PostComment>().apply {
        repeat(10) {
            add(PostComment(id = it))
        }
    }
    private val sourcePosts = mutableListOf<FeedPost>().apply {
        repeat(10) {
            add(FeedPost(id = it))
        }
    }
    private val initialState = HomeScreenState.Posts(posts = sourcePosts)
    // *********************************************************************************************

    private val _screenState = MutableLiveData<HomeScreenState>(initialState)
    val screenState: LiveData<HomeScreenState>
        get() = _screenState

    private var savedState: HomeScreenState = initialState

    fun showComments(feedPost: FeedPost) {
        savedState = _screenState.value ?: initialState
        _screenState.value = HomeScreenState.Comments(
            feedPost = feedPost,
            comments = sourceComments
        )
    }

    fun closeComments() {
        _screenState.value = savedState
    }

    fun updateCount(oldFeedPost: FeedPost, statisticItem: StatisticItem) {
        val currentState = _screenState.value
        if (currentState !is HomeScreenState.Posts) return

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

        _screenState.value = HomeScreenState.Posts(posts = newFeedPosts)
    }

    fun remove(feedPost: FeedPost) {
        val currentState = _screenState.value
        if (currentState !is HomeScreenState.Posts) return

        val feedPosts = currentState.posts.toMutableList()
        feedPosts.remove(feedPost)
        _screenState.value = HomeScreenState.Posts(posts = feedPosts)
    }
}