package uz.mobile.joybox.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentDTO(
    @SerialName("id") val id: Int?,
    @SerialName("body") val body: String?,
    @SerialName("type") val type: String?,
    @SerialName("created_at") val createdAt: String?,
    @SerialName("user_lesson") val userLessons: UserLessons?,
    @SerialName("rate") val rate: Float?
) {
    @Serializable
    data class UserLessons(
        @SerialName("user_id") val user: UpdateProfileResponse?
    )
}

