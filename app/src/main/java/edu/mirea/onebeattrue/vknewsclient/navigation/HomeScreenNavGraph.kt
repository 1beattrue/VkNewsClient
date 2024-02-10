package edu.mirea.onebeattrue.vknewsclient.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import edu.mirea.onebeattrue.vknewsclient.domain.entity.FeedPost

fun NavGraphBuilder.homeScreenNavGraph(
    newsFeedScreenContent: @Composable () -> Unit,
    commentsScreenContent: @Composable (FeedPost) -> Unit
) { // Nested Graph
    navigation(
        startDestination = Screen.NewsFeed.route,
        route = Screen.Home.route,
    ) {
        composable(Screen.NewsFeed.route) {
            newsFeedScreenContent()
        }

        composable(
            route = Screen.Comments.route,
            arguments = listOf(
                navArgument(Screen.KEY_FEED_POST) {
                    type = FeedPost.NavigationType
                }
            )
        ) {
            val feedPost = it.arguments?.getParcelable<FeedPost>(Screen.KEY_FEED_POST)
                ?: throw RuntimeException("Args is null")
            commentsScreenContent(feedPost)
        }
    }
}