package edu.mirea.onebeattrue.vknewsclient.domain

data class PostComment(
    val id: Long,
    val authorName: String,
    val authorAvatarUrl: String,
    val commentText:String,
    val publicationDate: String
)