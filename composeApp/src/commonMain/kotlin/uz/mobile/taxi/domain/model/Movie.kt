package uz.mobile.taxi.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val id: Int = 0,
    val description: String = "",
    val title: String = "",
    val url: String = "",
    val image: String = "",
    val genres: List<Tag> = emptyList(),
    val seasons: List<Season> = emptyList(),
    val isSubscribed: Boolean = false
) : JavaSerializable {

    val type get() : Type = if (seasons.isEmpty()) Type.MOVIE else Type.EPISODE
}
