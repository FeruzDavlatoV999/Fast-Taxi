package uz.mobile.joybox.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginDTO(
    @SerialName("access_token")
    val accessToken: String?,
    @SerialName("user")
    val user: UpdateProfileResponse?
) {

}