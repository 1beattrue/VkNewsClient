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
import androidx.compose.material.icons.rounded.FavoriteBorder
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.mirea.onebeattrue.vknewsclient.R

@Composable
fun PostCard() {
    Card(
        modifier = Modifier
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            PostHeader()

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Post text",
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Image(
                modifier = Modifier
                    .fillMaxWidth(),
                painter = painterResource(id = R.drawable.vk_logo),
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )
            Spacer(modifier = Modifier.height(8.dp))

            Statistics()
        }
    }
}

@Composable
private fun PostHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .clip(CircleShape)
                .size(64.dp),
            painter = painterResource(id = R.drawable.vk_logo),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(R.string.group_name),
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.post_time),
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
        Icon(
            imageVector = Icons.Rounded.MoreVert,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSecondary
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
            tint = MaterialTheme.colorScheme.onSecondary
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Composable
private fun Statistics() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
        ) {
            IconWithText(
                iconResId = R.drawable.ic_eye,
                text = stringResource(R.string.views_count)
            )
        }
        Row(
            modifier = Modifier
                .weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconWithText(
                iconResId = R.drawable.ic_share,
                text = stringResource(R.string.repost_count)
            )
            IconWithText(
                iconResId = R.drawable.ic_comments,
                text = stringResource(R.string.comment_count)
            )
            IconWithText(
                iconResId = R.drawable.ic_like_border,
                text = stringResource(R.string.like_count)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewLight() {
    VkNewsClientTheme(darkTheme = false) {
        PostCard()
    }
}

@Preview
@Composable
private fun PreviewDark() {
    VkNewsClientTheme(darkTheme = true) {
        PostCard()
    }
}