package edu.mirea.onebeattrue.vknewsclient.data.mapper

import edu.mirea.onebeattrue.vknewsclient.data.model.CommentsResponseDto
import edu.mirea.onebeattrue.vknewsclient.data.model.NewsFeedResponseDto
import edu.mirea.onebeattrue.vknewsclient.domain.FeedPost
import edu.mirea.onebeattrue.vknewsclient.domain.PostComment
import edu.mirea.onebeattrue.vknewsclient.domain.StatisticItem
import edu.mirea.onebeattrue.vknewsclient.domain.StatisticType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue

class NewsFeedMapper {
    fun mapResponseToPosts(responseDto: NewsFeedResponseDto): List<FeedPost> {
        val result = mutableListOf<FeedPost>()

        val posts = responseDto.content.posts
        val groups = responseDto.content.groups

        for (post in posts) {
            val group = groups.find { it.id == post.communityId.absoluteValue }
                ?: continue // знак id зависит от того опубликовал пост человек или группа
            val feedPost = FeedPost(
                id = post.id,
                communityId = post.communityId,
                communityName = group.name,
                publicationDate = mapTimestampToDate(post.date),
                communityImageUrl = group.imageUrl,
                contentText = post.text,
                contentImageUrl = post.attachments.firstOrNull()?.photo?.photoUrls?.lastOrNull()?.url, // пока только одно изображение
                statistics = listOf(
                    StatisticItem(type = StatisticType.LIKES, count = post.likes.count),
                    StatisticItem(type = StatisticType.COMMENTS, count = post.comments.count),
                    StatisticItem(type = StatisticType.SHARES, count = post.reposts.count),
                    StatisticItem(type = StatisticType.VIEWS, count = post.views.count),
                ),
                isLiked = post.likes.userLikes > 0
            )
            result.add(feedPost)
        }

        return result
    }

    fun mapResponseToComments(responseDto: CommentsResponseDto): List<PostComment> {
        val result = mutableListOf<PostComment>()

        val comments = responseDto.content.comments
        val profiles = responseDto.content.profiles

        for (comment in comments) {
            val profile = profiles.firstOrNull { it.id == comment.authorId } ?: continue
            val postComment = PostComment(
                id = comment.id,
                authorName = "${profile.firstName} ${profile.lastName}",
                authorAvatarUrl = profile.avatarUrl,
                commentText = comment.text,
                publicationDate = mapTimestampToDate(comment.date)
            )
            result.add(postComment)
        }

        return result
    }

    private fun mapTimestampToDate(timestampSeconds: Long): String {
        val timestampMilliseconds = timestampSeconds * 1000
        val date = Date(timestampMilliseconds)
        return SimpleDateFormat("d MMMM yyyy, hh:mm", Locale.getDefault()).format(date)
    }
}