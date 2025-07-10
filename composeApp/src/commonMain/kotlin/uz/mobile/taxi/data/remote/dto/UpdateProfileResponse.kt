package uz.mobile.taxi.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UpdateProfileResponse(
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("email")
    val email: String? = null,
    @SerialName("gender")
    val gender: String? = null,
    @SerialName("date")
    val date: String? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("profile")
    val profile: Profile? = null,
    @SerialName("status")
    val status: Status? = null,
    @SerialName("status_id")
    val statusId: Int? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null,
    @SerialName("phone")
    val phone: String? = null
) {
    @Serializable
    data class Profile(
        @SerialName("avatar")
        val avatar: String? = null,
        @SerialName("gender")
        val gender: String? = null,
        @SerialName("birthday")
        val birthday: String? = null,
        @SerialName("created_at")
        val createdAt: String? = null,
        @SerialName("email")
        val email: String? = null,
        @SerialName("first_name")
        val firstName: String? = null,
        @SerialName("id")
        val id: Int? = null,
        @SerialName("last_name")
        val lastName: String? = null,
        @SerialName("middle_name")
        val middleName: String? = null,
        @SerialName("updated_at")
        val updatedAt: String? = null,
        @SerialName("user_id")
        val userId: Int? = null,
    )

    @Serializable
    data class Status(
        @SerialName("code")
        val code: Int? = null,
        @SerialName("description")
        val description: String? = null,
        @SerialName("id")
        val id: Int? = null,
        @SerialName("name")
        val name: String? = null,
    )
}
