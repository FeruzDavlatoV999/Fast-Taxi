package uz.mobile.taxi.domain.model

import uz.mobile.taxi.data.remote.dto.SubscriptionsResponse


enum class Status(val value: Int) {
    INACTIVE(0),
    ACTIVE(1),
    EXPIRED(2),
    PENDING_FOR_PAYMENT(3);

    companion object {
        fun fromValue(value: Int): Status? {
            return entries.find { it.value == value }
        }
    }
}

fun processSubscriptionsByStatus(
    subscriptions: List<SubscriptionsResponse.Subscription?>?,
): Map<Status, List<SubscriptionsResponse.Subscription?>> {
    val result = subscriptions
        ?.filter { subscription ->
            val status = subscription?.status?.code?.let { Status.fromValue(it) }
            status != null
        }
        ?.groupBy { subscription ->
            Status.fromValue(subscription?.status?.code ?: 0) ?: Status.INACTIVE
        }
    return result ?: emptyMap()
}
