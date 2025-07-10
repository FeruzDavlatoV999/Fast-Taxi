package uz.mobile.taxi.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class PromoCodeResponse(
    @SerialName("code")
    val code: String? = null,
    @SerialName("promoCode")
    val promoCode: PromoCode? = null,
) {
    @Serializable
    data class PromoCode(
        @SerialName("auto_active")
        val autoActive: Int? = null,
        @SerialName("code")
        val code: String? = null,
        @SerialName("created_at")
        val createdAt: String? = null,
        @SerialName("title")
        val title: String? = null,
        @SerialName("message")
        val message: String? = null,
        @SerialName("deleted_at")
        val deletedAt: String? = null,
        @SerialName("expired_at")
        val expiredAt: String? = null,
        @SerialName("id")
        val id: Int? = null,
        @SerialName("max_use")
        val maxUse: Int? = null,
        @SerialName("name")
        val name: String? = null,
        @SerialName("promo_code_to")
        val promoCodeTo: String? = null,
        @SerialName("started_at")
        val startedAt: String? = null,
        @SerialName("type")
        val type: String? = null,
        @SerialName("updated_at")
        val updatedAt: String? = null,
        @SerialName("used_count")
        val usedCount: Int? = null,
        @SerialName("value")
        val value: Int? = null,
    )
}
