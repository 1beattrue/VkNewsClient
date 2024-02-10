package edu.mirea.onebeattrue.vknewsclient.navigation

import android.net.Uri
import com.google.gson.Gson
import edu.mirea.onebeattrue.vknewsclient.domain.entity.FeedPost

sealed class Screen(
    val route: String
) {
    object NewsFeed : Screen(ROUTE_NEWS_FEED)
    object Comments : Screen(ROUTE_COMMENTS) {

        private const val ROUTE_FOR_ARGS = "comments"
        fun getRouteWithArgs(feedPost: FeedPost): String {
            val feedPostJson = Gson().toJson(feedPost)
            return "$ROUTE_FOR_ARGS/${feedPostJson.encode()}" // передача объекта в формате Json
        }
    }

    object Home : Screen(ROUTE_HOME)
    object Favourite : Screen(ROUTE_FAVOURITE)
    object Profile : Screen(ROUTE_PROFILE)

    companion object {
        const val KEY_FEED_POST = "feed_post"

        const val ROUTE_COMMENTS = "comments/{$KEY_FEED_POST}"
        const val ROUTE_NEWS_FEED = "news_feed"

        const val ROUTE_HOME = "home"
        const val ROUTE_FAVOURITE = "favourite"
        const val ROUTE_PROFILE = "profile"
    }
}

fun String.encode(): String =
    Uri.encode(this) // для правильной передачи строк, содержащих спецсимволы (например '/')
