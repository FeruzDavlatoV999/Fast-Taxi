package uz.mobile.taxi.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import uz.mobile.taxi.domain.model.Movie
import uz.mobile.taxi.domain.model.Season

@Serializable
data class MoviesResponse(
    @SerialName("courses")
    val movies: List<MovieDetailResponse>? = null,
    @SerialName("paginator")
    val paginator: Paginator? = null,
)


fun MovieDetailResponse.toMovie() = Movie(
    id = id ?: 0,
    title = title.orEmpty(),
    description = description.orEmpty(),
    url = resource.orEmpty(),
    image = thumbnail.orEmpty(),
    genres = genres?.toTags().orEmpty(),
    seasons = chapters?.toSeasons().orEmpty(),
    isSubscribed = isSubscribed ?: false
)

fun Chapter.toSeason() = Season(
    id = id ?: 0,
    title = title.orEmpty(),
    movieId = courseId ?: 0,
    episodeCount = lessonsCount ?: 0,
    episodes = lessons?.toMovies().orEmpty()
)

fun List<Chapter>.toSeasons() = map { it.toSeason() }

fun List<MovieDetailResponse>.toMovies(): List<Movie> = orEmpty().map { it.toMovie() }
