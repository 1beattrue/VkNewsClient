package edu.mirea.onebeattrue.vknewsclient.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.mirea.onebeattrue.vknewsclient.R
import edu.mirea.onebeattrue.vknewsclient.domain.FeedPost
import edu.mirea.onebeattrue.vknewsclient.domain.StatisticItem
import edu.mirea.onebeattrue.vknewsclient.domain.StatisticType
import java.lang.IllegalStateException

@Composable
fun PostCard(
    modifier: Modifier = Modifier, // является хорошей практикой
    feedPost: FeedPost
) {
    Card(
        modifier = modifier
            .padding(8.dp)
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

            Image(
                modifier = Modifier
                    .fillMaxWidth(),
                painter = painterResource(id = feedPost.contentImageResId),
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )
            Spacer(modifier = Modifier.height(8.dp))

            Statistics(statistics = feedPost.statistics)
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
        Image(
            modifier = Modifier
                .clip(CircleShape)
                .size(64.dp),
            painter = painterResource(id = feedPost.avatarResId),
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

@Composable
private fun IconWithText(
    iconResId: Int,
    text: String
) {
    Row {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
private fun Statistics(
    statistics: List<StatisticItem>
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
        ) {
            val viewsItem = statistics.getItemByType(StatisticType.VIEWS)
            IconWithText(
                iconResId = R.drawable.ic_eye,
                text = viewsItem.count.toString()
            )
        }
        Row(
            modifier = Modifier
                .weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val sharesItem = statistics.getItemByType(StatisticType.SHARES)
            IconWithText(
                iconResId = R.drawable.ic_share,
                text = sharesItem.count.toString()
            )
            val commentsItem = statistics.getItemByType(StatisticType.COMMENTS)
            IconWithText(
                iconResId = R.drawable.ic_comments,
                text = commentsItem.count.toString()
            )
            val likesItem = statistics.getItemByType(StatisticType.LIKES)
            IconWithText(
                iconResId = R.drawable.ic_like_border,
                text = likesItem.count.toString()
            )
        }
    }
}

private fun List<StatisticItem>.getItemByType(type: StatisticType): StatisticItem {
    return this.find { it.type == type }
        ?: throw IllegalStateException("Item with this type was not found")
}


@Preview
@Composable
private fun PreviewLight() {
    VkNewsClientTheme(darkTheme = false) {
        PostCard(feedPost = FeedPost())
    }
}

@Preview
@Composable
private fun PreviewDark() {
    VkNewsClientTheme(darkTheme = true) {
        PostCard(feedPost = FeedPost())
    }
}