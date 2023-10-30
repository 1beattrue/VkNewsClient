package edu.mirea.onebeattrue.vknewsclient.domain

import edu.mirea.onebeattrue.vknewsclient.R

data class PostComment(
    val id: Int,
    val authorName: String = "Author",
    val avatarResId: Int = R.drawable.vk_logo,
    val commentText:String = "Long comment text",
    val publicationDate: String = "14:00"
)