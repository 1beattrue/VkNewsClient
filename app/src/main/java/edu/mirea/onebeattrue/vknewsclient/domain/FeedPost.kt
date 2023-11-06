package edu.mirea.onebeattrue.vknewsclient.domain

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.Gson
import edu.mirea.onebeattrue.vknewsclient.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class FeedPost(
    val id: Int = 0,
    val communityName: String = "Group name",
    val publicationDate: String = "27.10.2023",
    val avatarResId: Int = R.drawable.vk_logo,
    val contentText: String = "Post text",
    val contentImageResId: Int = R.drawable.vk_logo,
    val statistics: List<StatisticItem> = listOf(
        StatisticItem(type = StatisticType.VIEWS, count = 666),
        StatisticItem(type = StatisticType.SHARES, count = 2),
        StatisticItem(type = StatisticType.COMMENTS, count = 42),
        StatisticItem(type = StatisticType.LIKES, count = 69)
    )
) : Parcelable {
    companion object {
        val NavigationType = object : NavType<FeedPost>(false) { // custom NavType
            override fun get(bundle: Bundle, key: String): FeedPost? {
                return bundle.getParcelable(key)
            }

            override fun parseValue(value: String): FeedPost {
                return Gson().fromJson(value, FeedPost::class.java)
            }

            override fun put(bundle: Bundle, key: String, value: FeedPost) {
                bundle.putParcelable(key, value)
            }

        }
    }
}