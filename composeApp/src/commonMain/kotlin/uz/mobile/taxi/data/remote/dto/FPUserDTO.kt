package uz.mobile.taxi.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FPUserDTO(
    @SerialName("first_name")
    val firstName: String? = null,
    @SerialName("code")
    val code: String? = null,
    @SerialName("last_name")
    val lastName: String? = null,
    @SerialName("password")
    val password: String? = null,
    @SerialName("password_confirmation")
    val passwordConfirmation: String? = null,
    @SerialName("phone")
    val phone: String? = null
)

