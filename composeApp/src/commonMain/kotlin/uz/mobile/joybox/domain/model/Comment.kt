package uz.mobile.joybox.domain.model

data class Comment(
    val id: Int,
    val comment: String,
    val userId: Int,
    val userName: String,
    val userPhoto: String,
    val date: String,
    val rating:Float
)
