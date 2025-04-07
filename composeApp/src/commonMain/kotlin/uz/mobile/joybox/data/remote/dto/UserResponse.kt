package uz.mobile.joybox.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UserResponse(
    @SerialName("user")
    val user: UpdateProfileResponse? = null,
)
