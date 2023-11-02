package edu.mirea.onebeattrue.vknewsclient.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.mirea.onebeattrue.vknewsclient.domain.FeedPost
import edu.mirea.onebeattrue.vknewsclient.domain.PostComment
import edu.mirea.onebeattrue.vknewsclient.presentation.viewmodels.CommentsViewModel
import edu.mirea.onebeattrue.vknewsclient.presentation.viewmodels.CommentsViewModelFactory
import edu.mirea.onebeattrue.vknewsclient.ui.CommentItem
import edu.mirea.onebeattrue.vknewsclient.ui.states.CommentsScreenState

@Composable
fun CommentsScreen(
    onBackPressed: () -> Unit,
    feedPost: FeedPost
) {
    val viewModel: CommentsViewModel = viewModel(
        factory = CommentsViewModelFactory(feedPost)
    )
    val screenState = viewModel.screenState.observeAsState(CommentsScreenState.Initial)

    when(val currentState = screenState.value) {
        is CommentsScreenState.Comments -> {
            Comments(
                feedPost = currentState.feedPost,
                comments = currentState.comments,
                onBackPressed = {
                    onBackPressed()
                }
            )
        }
        is CommentsScreenState.Initial -> {}
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Comments(
    feedPost: FeedPost,
    comments: List<PostComment>,
    onBackPressed: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Comments for FeedPost with Id: ${feedPost.id}",
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
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(
                top = 8.dp,
                start = 8.dp,
                end = 8.dp,
                bottom = 96.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = comments, key = { it.id }) { comment ->
                CommentItem(comment = comment)
            }

        }
    }
}