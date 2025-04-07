package uz.mobile.joybox.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class LiveDatesResponse(
    @SerialName("dates")
    val dates: List<Date>? = null,
)

@Serializable
data class Date(
    @SerialName("date")
    val date: String? = null,
    @SerialName("id")
    val id: Int? = null,
)
