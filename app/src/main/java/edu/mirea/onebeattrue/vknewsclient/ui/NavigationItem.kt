package edu.mirea.onebeattrue.vknewsclient.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.ui.graphics.vector.ImageVector
import edu.mirea.onebeattrue.vknewsclient.R

sealed class NavigationItem(
    val titleResId: Int,
    val icon: ImageVector
) {
    object Home : NavigationItem(
        titleResId = R.string.navigation_item_main,
        icon = Icons.Rounded.Home
    )

    object Favourite : NavigationItem(
        titleResId = R.string.navigation_item_favourite,
        icon = Icons.Rounded.Favorite
    )

    object Profile : NavigationItem(
        titleResId = R.string.navigation_item_profile,
        icon = Icons.Rounded.Person
    )
}