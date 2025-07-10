package uz.mobile.taxi.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UserResponse(
    @SerialName("user")
    val user: UpdateProfileResponse? = null,
)
