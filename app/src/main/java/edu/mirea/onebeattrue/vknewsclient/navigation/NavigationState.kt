package edu.mirea.onebeattrue.vknewsclient.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

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

    fun navigateTo(route: String) {
        navHostController.navigate(route) {
            launchSingleTop = true
            popUpTo(navHostController.graph.findStartDestination().id) {
                saveState = true
            }
            restoreState = true
        }
    }

    fun navigateToComments() { // чтобы экран комментариев оставался открытым при возвращении к нему через bnm
        navHostController.navigate(Screen.Comments.route)
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