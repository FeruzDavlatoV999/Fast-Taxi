package uz.mobile.joybox.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryResponse(
    @SerialName("categories")
    val categories: List<Category>? = null,
    @SerialName("paginator")
    val paginator: Paginator? = null,
    ) {
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
        @SerialName("courses")
        val courses: List<MovieDetailResponse>? = null,
    )
}
