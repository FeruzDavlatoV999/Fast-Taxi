package uz.mobile.taxi.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class MovieDetailResponse(
    @SerialName("categories")
    val categories: List<Category>? = null,
    @SerialName("chapters")
    val chapters: List<Chapter>? = null,
    @SerialName("course_currency")
    val courseCurrency: String? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("duration")
    val duration: Int? = null,
    @SerialName("published_at")
    val publishAt: String? = null,
    @SerialName("genres")
    val genres: List<TagDTO>? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("index")
    val index: Int? = null,
    @SerialName("is_subscribed")
    val isSubscribed: Boolean? = null,
    @SerialName("lessons_count")
    val lessonsCount: Int? = null,
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
data class Category(
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("slug")
    val slug: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null,
)

@Serializable
data class Chapter(
    @SerialName("course_id")
    val courseId: Int? = null,
    @SerialName("course_index")
    val courseIndex: Int? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("lessons")
    val lessons: List<MovieDetailResponse>? = null,
    @SerialName("lessons_count")
    val lessonsCount: Int? = null,
    @SerialName("title")
    val title: String? = null,
)

