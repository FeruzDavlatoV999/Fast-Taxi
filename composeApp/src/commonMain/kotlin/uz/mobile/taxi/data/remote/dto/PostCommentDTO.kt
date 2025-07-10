package uz.mobile.taxi.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostCommentDTO(
    @SerialName("body") val body: String,
    @SerialName("user_lesson_id") val userLessonId: Int,
    @SerialName("user_lesson_comment_id") val userLessonCommentId: Int? = null,
    @SerialName("type") val type: String,
    @SerialName("rate") val rate: Float,
)