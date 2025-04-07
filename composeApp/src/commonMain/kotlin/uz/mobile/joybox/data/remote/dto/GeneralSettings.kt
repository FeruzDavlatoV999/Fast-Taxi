package uz.mobile.joybox.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class GeneralSettings(
    @SerialName("generalSettings")
    val generalSettings: GeneralSetting? = null,
) {
    @Serializable
    data class GeneralSetting(
        @SerialName("address")
        val address: String? = null,
        @SerialName("app_logo")
        val appLogo: String? = null,
        @SerialName("app_name")
        val appName: String? = null,
        @SerialName("created_at")
        val createdAt: String? = null,
        @SerialName("email")
        val email: String? = null,
        @SerialName("facebook")
        val facebook: String? = null,
        @SerialName("id")
        val id: Int? = null,
        @SerialName("instagram")
        val instagram: String? = null,
        @SerialName("phone")
        val phone: String? = null,
        @SerialName("privacy_content")
        val privacyContent: String? = null,
        @SerialName("privacy_title")
        val privacyTitle: String? = null,
        @SerialName("telegram")
        val telegram: String? = null,
        @SerialName("terms_content")
        val termsContent: String? = null,
        @SerialName("terms_title")
        val termsTitle: String? = null,
        @SerialName("updated_at")
        val updatedAt: String? = null,
        @SerialName("youtobe")
        val youtobe: String? = null,
    )
}
