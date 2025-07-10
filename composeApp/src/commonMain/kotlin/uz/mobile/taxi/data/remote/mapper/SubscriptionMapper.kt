package uz.mobile.taxi.data.remote.mapper

import uz.mobile.taxi.data.remote.dto.SubscriptionsResponse
import uz.mobile.taxi.domain.model.Subscription


fun SubscriptionsResponse.Subscription.toSubscription(): Subscription {
    return Subscription(
        id = id ?: -1,
        name = term?.name.orEmpty(),
        description = term?.description.orEmpty(),
        price = currency?.pivot?.price ?: 0L,
        currency = currency?.iso4217.orEmpty(),
        oldPrice = currency?.pivot?.oldPrice ?: 0L,
        bestSeller = isBestseller ?: false
    )
}


fun List<SubscriptionsResponse.Subscription>.toSubsList(): List<Subscription> = map {
    it.toSubscription()
}