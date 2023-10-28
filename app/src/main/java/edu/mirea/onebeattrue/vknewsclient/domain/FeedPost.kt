package edu.mirea.onebeattrue.vknewsclient.domain

import edu.mirea.onebeattrue.vknewsclient.R

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
)