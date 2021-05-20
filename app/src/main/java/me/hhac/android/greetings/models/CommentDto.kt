package me.hhac.android.greetings.models

import java.io.Serializable

data class CommentDto(
    val commentText: String? = null,
    val forumTopicId: Long? = null,
    val imageData: String? = null
) : Serializable