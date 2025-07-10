package uz.mobile.taxi.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Season(
    val id: Int?,
    val title: String?,
    val movieId: Int?,
    val episodeCount: Int?,
    val episodes: List<Movie>
) : JavaSerializable