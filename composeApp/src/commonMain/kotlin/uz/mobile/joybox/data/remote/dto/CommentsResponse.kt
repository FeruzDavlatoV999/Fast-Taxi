package uz.mobile.joybox.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentsResponse(
    @SerialName("userLessonComments") val userLessonComments: List<CommentDTO>,
    @SerialName("paginator") val paginator: Paginator?
)