package edu.mirea.onebeattrue.vknewsclient.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.mirea.onebeattrue.vknewsclient.R
import edu.mirea.onebeattrue.vknewsclient.domain.entity.FeedPost
import edu.mirea.onebeattrue.vknewsclient.domain.entity.PostComment
import edu.mirea.onebeattrue.vknewsclient.presentation.getApplicationComponent
import edu.mirea.onebeattrue.vknewsclient.presentation.states.CommentsScreenState
import edu.mirea.onebeattrue.vknewsclient.presentation.viewmodels.CommentsViewModel
import edu.mirea.onebeattrue.vknewsclient.ui.CommentItem
import edu.mirea.onebeattrue.vknewsclient.ui.theme.VkColor

@Composable
fun CommentsScreen(
    paddingValues: PaddingValues,
    onBackPressed: () -> Unit,
    feedPost: FeedPost
) {
    val component = getApplicationComponent()
        .getCommentScreenComponentFactory()
        .create(feedPost)

    val viewModel: CommentsViewModel = viewModel(
        factory = component.getViewModelFactory()
    )
    val screenState = viewModel.screenState.collectAsState(CommentsScreenState.Initial)

    CommentsScreenContent(
        screenState = screenState,
        onBackPressed = onBackPressed,
        viewModel = viewModel,
        paddingValues = paddingValues,
        feedPost = feedPost
    )
}

@Composable
private fun CommentsScreenContent(
    screenState: State<CommentsScreenState>,
    onBackPressed: () -> Unit,
    viewModel: CommentsViewModel,
    paddingValues: PaddingValues,
    feedPost: FeedPost
) {
    when (val currentState = screenState.value) {
        is CommentsScreenState.Comments -> {
            Comments(
                comments = currentState.comments,
                onBackPressed = {
                    onBackPressed()
                },
                paddingValues = paddingValues,
                viewModel = viewModel,
                feedPost = feedPost
            )
        }

        is CommentsScreenState.Loading -> {
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = VkColor)
            }
        }

        is CommentsScreenState.Initial -> {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Comments(
    comments: List<PostComment>,
    onBackPressed: () -> Unit,
    paddingValues: PaddingValues,
    viewModel: CommentsViewModel,
    feedPost: FeedPost
) {
    Scaffold(
        modifier = Modifier.padding(paddingValues),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.comments_title),
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBackPressed() }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { commentsPaddingValues ->
        LazyColumn(
            modifier = Modifier.padding(commentsPaddingValues),
            contentPadding = PaddingValues(
                top = 8.dp,
                start = 8.dp,
                end = 8.dp,
                bottom = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = comments, key = { it.id }) { comment ->
                CommentItem(comment = comment)
            }
            item {
                SideEffect {
                    viewModel.loadComments(feedPost = feedPost)
                }
            }
        }
    }
}