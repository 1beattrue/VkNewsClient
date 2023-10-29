package edu.mirea.onebeattrue.vknewsclient.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import edu.mirea.onebeattrue.vknewsclient.ui.NavigationItem

class NavigationState(
    val navHostController: NavHostController
) {
    fun navigateTo(item: NavigationItem) {
        navHostController.navigate(item.screen.route) {
            launchSingleTop = true
            popUpTo(navHostController.graph.startDestinationId) {
                saveState = true
            }
            restoreState = true
        }
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