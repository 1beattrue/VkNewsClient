package edu.mirea.onebeattrue.vknewsclient.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import edu.mirea.onebeattrue.vknewsclient.domain.entity.FeedPost

class NavigationState(
    val navHostController: NavHostController
) {
    fun navigateTo(item: NavigationItem) {
        navHostController.navigate(item.screen.route) {
            launchSingleTop = true
            popUpTo(navHostController.graph.findStartDestination().id) { // работает с вложенными графами
                saveState = true
            }
            restoreState = true
        }
    }

    fun navigateToComments(feedPost: FeedPost) { // чтобы экран комментариев оставался открытым при возвращении к нему через bnm
        navHostController.navigate(Screen.Comments.getRouteWithArgs(feedPost))
    }
}

@Composable
fun rememberNavigationState(
    navHostController: NavHostController = rememberNavController()
): NavigationState {
    return remember {
        NavigationState(navHostController)
    }
}