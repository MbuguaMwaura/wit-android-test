package me.hhac.android.greetings.models

data class CommentResponseDto(
    val id: Long?,
    val commenter: Creator? = null,
    val commentText: String? = null,
    val commentImage:String? = null,
    val isActive: Boolean? = null,
    val dateCreated : String? = null
)

data class Creator(
    var name : String? = null,
    var photoUrl : String? = null,
    var dateJoined : String? = null,
    var email : String? = null,
    var phoneNumber : String? = null
)