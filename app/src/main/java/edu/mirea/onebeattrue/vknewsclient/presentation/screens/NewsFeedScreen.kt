package edu.mirea.onebeattrue.vknewsclient.presentation.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.mirea.onebeattrue.vknewsclient.domain.entity.FeedPost
import edu.mirea.onebeattrue.vknewsclient.presentation.states.NewsFeedScreenState
import edu.mirea.onebeattrue.vknewsclient.presentation.viewmodels.NewsFeedViewModel
import edu.mirea.onebeattrue.vknewsclient.ui.PostCard
import edu.mirea.onebeattrue.vknewsclient.ui.theme.VkColor


@Composable
fun NewsFeedScreen(
    paddingValues: PaddingValues,
    onCommentsClickListener: (FeedPost) -> Unit
) {
    val viewModel: NewsFeedViewModel = viewModel() // нужен impl
    val screenState = viewModel.screenState.collectAsState(NewsFeedScreenState.Initial)

    when (val currentState = screenState.value) {
        is NewsFeedScreenState.Posts -> {
            FeedPosts(
                posts = currentState.posts,
                viewModel = viewModel,
                onCommentsClickListener = onCommentsClickListener,
                nextDataIsLoading = currentState.nextDataIsLoading,
                paddingValues = paddingValues
            )
        }

        is NewsFeedScreenState.Loading -> {
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = VkColor)
            }
        }

        is NewsFeedScreenState.Initial -> {}
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun FeedPosts(
    posts: List<FeedPost>,
    viewModel: NewsFeedViewModel,
    onCommentsClickListener: (FeedPost) -> Unit,
    nextDataIsLoading: Boolean = false,
    paddingValues: PaddingValues
) {
    LazyColumn(
        modifier = Modifier.padding(paddingValues = paddingValues),
        contentPadding = PaddingValues(
            top = 8.dp,
            start = 8.dp,
            end = 8.dp,
            bottom = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items = posts, key = { it.id }) { feedPost ->
            val dismissState = rememberDismissState()
            if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                viewModel.deletePost(feedPost)
            }

            SwipeToDismiss(
                modifier = Modifier.animateItemPlacement(),
                state = dismissState,
                background = {},
                directions = setOf(DismissDirection.EndToStart),
                dismissContent = {
                    PostCard(
                        feedPost = feedPost,
                        onLikesClickListener = { _ ->
                            viewModel.changeLikeStatus(feedPost)
                        },
                        onCommentsClickListener = {
                            onCommentsClickListener(feedPost)
                        },
                    )
                },
            )
        }
        item {
            if (nextDataIsLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = VkColor)
                }
            } else {
                SideEffect {
                    viewModel.loadNextRecommendations()
                }
            }
        }
    }
}