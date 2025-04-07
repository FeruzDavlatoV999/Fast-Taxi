package uz.mobile.joybox.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class LiveResponse(
    @SerialName("live_url")
    val liveUrl: LiveUrl? = null,
) {
    @Serializable
    data class LiveUrl(
        @SerialName("created_at")
        val createdAt: String? = null,
        @SerialName("id")
        val id: Int? = null,
        @SerialName("updated_at")
        val updatedAt: String? = null,
        @SerialName("url")
        val url: String? = null,
    )
}
