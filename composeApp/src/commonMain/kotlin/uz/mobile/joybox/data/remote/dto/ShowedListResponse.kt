package uz.mobile.joybox.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import uz.mobile.joybox.domain.model.Movie


@Serializable
data class ShowedListResponse(
    @SerialName("paginator")
    val paginator: Paginator? = null,
    @SerialName("userWatchHistories")
    val userWatchHistories: List<UserWatchHistory>? = null,
)

@Serializable
data class UserWatchHistory(
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("model")
    val model: Model? = null,
    @SerialName("type")
    val type: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null,
    @SerialName("watched_duration")
    val watchedDuration: Int? = null,
)

@Serializable
data class Model(
    @SerialName("categories")
    val categories: List<Category?>? = null,
    @SerialName("chapters")
    val chapters: List<Chapter>? = null,
    @SerialName("genres")
    val genres: List<TagDTO>? = null,
    @SerialName("chapter_id")
    val chapterId: Int? = null,
    @SerialName("chapter_index")
    val chapterIndex: Int? = null,
    @SerialName("course_id")
    val courseId: Int? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("duration")
    val duration: Int? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("index")
    val index: String? = null,
    @SerialName("is_free")
    val isFree: Boolean? = null,
    @SerialName("is_subscribed")
    val isSubscribed: Boolean? = null,
    @SerialName("lessons_count")
    val lessonsCount: Int? = null,
    @SerialName("master")
    val master: String? = null,
    @SerialName("progress")
    val progress: Int? = null,
    @SerialName("resource")
    val resource: String? = null,
    @SerialName("seo_description")
    val seoDescription: String? = null,
    @SerialName("seo_keyword")
    val seoKeyword: String? = null,
    @SerialName("seo_title")
    val seoTitle: String? = null,
    @SerialName("status")
    val status: Status? = null,
    @SerialName("thumbnail")
    val thumbnail: String? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("trailer")
    val trailer: String? = null,
)


fun UserWatchHistory.toHistoryMovie() = Movie(
    id = model?.id ?: 0,
    title = model?.title.orEmpty(),
    description = model?.description.orEmpty(),
    url = model?.resource.orEmpty(),
    image = model?.thumbnail.orEmpty(),
    genres = model?.genres?.toTags().orEmpty(),
    seasons = model?.chapters?.toSeasons().orEmpty()
)


fun List<UserWatchHistory>.toHistoryMovie(): List<Movie> = orEmpty().map { it.toHistoryMovie() }
