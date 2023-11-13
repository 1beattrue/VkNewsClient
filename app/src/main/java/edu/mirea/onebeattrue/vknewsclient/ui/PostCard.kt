package edu.mirea.onebeattrue.vknewsclient.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import edu.mirea.onebeattrue.vknewsclient.R
import edu.mirea.onebeattrue.vknewsclient.domain.FeedPost
import edu.mirea.onebeattrue.vknewsclient.domain.StatisticItem
import edu.mirea.onebeattrue.vknewsclient.domain.StatisticType

@Composable
fun PostCard(
    modifier: Modifier = Modifier,
    feedPost: FeedPost,
    onLikesClickListener: (StatisticItem) -> Unit,
    onCommentsClickListener: (StatisticItem) -> Unit,
    onSharesClickListener: (StatisticItem) -> Unit,
    onViewsClickListener: (StatisticItem) -> Unit
) {
    Card(
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(8.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            PostHeader(feedPost = feedPost)

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = feedPost.contentText,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))

            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                model = feedPost.contentImageUrl,
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )
            Spacer(modifier = Modifier.height(8.dp))

            Statistics(
                statistics = feedPost.statistics,
                onLikesClickListener = onLikesClickListener,
                onCommentsClickListener = onCommentsClickListener,
                onSharesClickListener = onSharesClickListener,
                onViewsClickListener = onViewsClickListener,
                isFavourite = feedPost.isLiked
            )
        }
    }
}

@Composable
private fun PostHeader(
    feedPost: FeedPost
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .clip(CircleShape)
                .size(64.dp),
            model = feedPost.communityImageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = feedPost.communityName,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = feedPost.publicationDate,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
        Icon(
            imageVector = Icons.Rounded.MoreVert,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

private fun StatisticItem.formattedCount(): String {
    return if (count >= 100_000) {
        String.format("%sK", count / 1000)
    } else if (count >= 1000) {
        String.format("%.1fK", count / 1000f)
    } else if (count == 0) {
        ""
    } else {
        count.toString()
    }
}

@Composable
private fun Statistics(
    statistics: List<StatisticItem>,
    onLikesClickListener: (StatisticItem) -> Unit,
    onCommentsClickListener: (StatisticItem) -> Unit,
    onSharesClickListener: (StatisticItem) -> Unit,
    onViewsClickListener: (StatisticItem) -> Unit,
    isFavourite: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(.4f)
        ) {
            val viewsItem = statistics.getItemByType(StatisticType.VIEWS)
            IconWithText(
                iconResId = R.drawable.ic_eye,
                text = viewsItem.formattedCount(),
                onItemClickListener = {
                    onViewsClickListener(viewsItem)
                }
            )
        }
        Row(
            modifier = Modifier.weight(.6f),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            val sharesItem = statistics.getItemByType(StatisticType.SHARES)
            IconWithText(
                iconResId = R.drawable.ic_share,
                text = sharesItem.formattedCount(),
                onItemClickListener = {
                    onSharesClickListener(sharesItem)
                }
            )
            val commentsItem = statistics.getItemByType(StatisticType.COMMENTS)
            IconWithText(
                iconResId = R.drawable.ic_comments,
                text = commentsItem.formattedCount(),
                onItemClickListener = {
                    onCommentsClickListener(commentsItem)
                }
            )
            val likesItem = statistics.getItemByType(StatisticType.LIKES)
            IconWithText(
                iconResId = if (isFavourite) R.drawable.ic_like else R.drawable.ic_like_border,
                text = likesItem.formattedCount(),
                onItemClickListener = {
                    onLikesClickListener(likesItem)
                },
                tint = if (isFavourite)
                    Color.Red
                else
                    MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
private fun IconWithText(
    iconResId: Int,
    text: String,
    onItemClickListener: () -> Unit,
    tint: Color = MaterialTheme.colorScheme.onSecondaryContainer
) {
    Row(
        modifier = Modifier.clickable {
            onItemClickListener()
        }
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            tint = tint
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

private fun List<StatisticItem>.getItemByType(type: StatisticType): StatisticItem {
    return this.find { it.type == type }
        ?: throw IllegalStateException("Item with this type was not found")
}