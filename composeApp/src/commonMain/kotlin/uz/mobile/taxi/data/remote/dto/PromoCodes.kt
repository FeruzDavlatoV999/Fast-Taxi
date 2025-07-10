package uz.mobile.taxi.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class PromoCodes(
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
        @SerialName("subscriptions")
        val subscriptions: List<Subscription?>? = null,
        @SerialName("type")
        val type: String? = null,
        @SerialName("updated_at")
        val updatedAt: String? = null,
        @SerialName("used_count")
        val usedCount: Int? = null,
        @SerialName("value")
        val value: Int? = null,
    ) {
        @Serializable
        data class Subscription(
            @SerialName("authors")
            val authors: String? = null,
            @SerialName("created_at")
            val createdAt: String? = null,
            @SerialName("currencies")
            val currencies: List<Currency?>? = null,
            @SerialName("deleted_at")
            val deletedAt: String? = null,
            @SerialName("id")
            val id: Int? = null,
            @SerialName("is_subscribed")
            val isSubscribed: Boolean? = null,
            @SerialName("pivot")
            val pivot: Pivot? = null,
            @SerialName("product_by")
            val productBy: String? = null,
            @SerialName("product_type")
            val productType: String? = null,
            @SerialName("status_id")
            val statusId: Int? = null,
            @SerialName("subscription_language")
            val subscriptionLanguage: List<SubscriptionLanguage?>? = null,
            @SerialName("subscription_type_id")
            val subscriptionTypeId: Int? = null,
            @SerialName("type")
            val type: Int? = null,
            @SerialName("updated_at")
            val updatedAt: String? = null,
        ) {
            @Serializable
            data class Currency(
                @SerialName("country")
                val country: String? = null,
                @SerialName("id")
                val id: Int? = null,
                @SerialName("iso_4217")
                val iso4217: String? = null,
                @SerialName("local_name")
                val localName: String? = null,
                @SerialName("name")
                val name: String? = null,
                @SerialName("pivot")
                val pivot: Pivot? = null,
            ) {
                @Serializable
                data class Pivot(
                    @SerialName("currency_id")
                    val currencyId: Int? = null,
                    @SerialName("price")
                    val price: Int? = null,
                    @SerialName("subscription_id")
                    val subscriptionId: Int? = null,
                )
            }
            @Serializable
            data class Pivot(
                @SerialName("promo_code_id")
                val promoCodeId: Int? = null,
                @SerialName("subscription_id")
                val subscriptionId: Int? = null,
            )
            @Serializable
            data class SubscriptionLanguage(
                @SerialName("deleted_at")
                val deletedAt: String? = null,
                @SerialName("description")
                val description: String? = null,
                @SerialName("id")
                val id: Int? = null,
                @SerialName("language_id")
                val languageId: String? = null,
                @SerialName("name")
                val name: String? = null,
                @SerialName("subscription_id")
                val subscriptionId: Int? = null,
            )
        }
    }
}
