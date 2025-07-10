package uz.mobile.taxi.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class SubscriptionsResponse(
    @SerialName("paginator")
    val paginator: Paginator? = null,
    @SerialName("subscriptions")
    val subscriptions: List<Subscription>? = null,
) {
    @Serializable
    data class Subscription(
        @SerialName("authors")
        val authors: String? = null,
        @SerialName("created_at")
        val createdAt: String? = null,
        @SerialName("currencies")
        val currencies: List<Currency?>? = null,
        @SerialName("currency")
        val currency: Currency? = null,
        @SerialName("statuses")
        val statuses: Statuses? = null,
        @SerialName("deleted_at")
        val deletedAt: String? = null,
        @SerialName("expired_at")
        val expiredAt: String? = null,
        @SerialName("id")
        val id: Int? = null,
        @SerialName("is_subscribed")
        val isSubscribed: Boolean? = null,
        @SerialName("product_by")
        val productBy: String? = null,
        @SerialName("product_type")
        val productType: String? = null,
        @SerialName("promo_codes")
        val promoCodes: List<PromoCode>? = null,
        @SerialName("sold")
        val sold: Int? = null,
        @SerialName("status")
        val status: Status? = null,
        @SerialName("status_id")
        val statusId: Int? = null,
        @SerialName("subscription_courses")
        val subscriptionCourses: List<SubscriptionCourse?>? = null,
        @SerialName("subscription_language")
        val subscriptionLanguage: List<SubscriptionLanguage?>? = null,
        @SerialName("subscription_type")
        val subscriptionType: SubscriptionType? = null,
        @SerialName("subscription_type_id")
        val subscriptionTypeId: Int? = null,
        @SerialName("term")
        val term: Term? = null,
        @SerialName("type")
        val type: Int? = null,
        @SerialName("updated_at")
        val updatedAt: String? = null,
        @SerialName("is_bestseller")
        val isBestseller: Boolean? = null,
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
                val price: Long? = null,
                @SerialName("subscription_id")
                val subscriptionId: Int? = null,
                @SerialName("old_price")
                val oldPrice: Long? = null,
            )
        }

        @Serializable
        data class PromoCode(
            @SerialName("subscription_id")
            val subscriptionId: Int? = null,
            @SerialName("promo_code_id")
            val promoCodeId: Int? = null,
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

        @Serializable
        data class SubscriptionCourse(
            @SerialName("course_id")
            val courseId: Int? = null,
            @SerialName("created_at")
            val createdAt: String? = null,
            @SerialName("deleted_at")
            val deletedAt: String? = null,
            @SerialName("subscription_id")
            val subscriptionId: Int? = null,
            @SerialName("updated_at")
            val updatedAt: String? = null,
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

        @Serializable
        data class SubscriptionType(
            @SerialName("code")
            val code: Int? = null,
            @SerialName("created_at")
            val createdAt: String? = null,
            @SerialName("description")
            val description: String? = null,
            @SerialName("id")
            val id: Int? = null,
            @SerialName("name")
            val name: String? = null,
            @SerialName("period")
            val period: Int? = null,
            @SerialName("post_access")
            val postAccess: Int? = null,
            @SerialName("updated_at")
            val updatedAt: String? = null,
        )

        @Serializable
        data class Term(
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

        @Serializable
        data class Statuses(
            @SerialName("Inactive")
            val Inactive: Int? = null,
            @SerialName("Active")
            val Active: Int? = null,
            @SerialName("Expired")
            val Expired: Int? = null,
            @SerialName("PendingForPayment")
            val PendingForPayment: Int? = null,
        )
    }
}