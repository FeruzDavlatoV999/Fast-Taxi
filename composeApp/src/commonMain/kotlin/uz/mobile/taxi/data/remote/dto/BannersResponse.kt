package uz.mobile.taxi.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import uz.mobile.taxi.domain.model.Banners


@Serializable
data class BannersResponse(
    @SerialName("banners")
    val banners: List<Banner>? = null,
    @SerialName("paginator")
    val paginator: Paginator? = null,
) {
    @Serializable
    data class Banner(
        @SerialName("created_at")
        val createdAt: String? = null,
        @SerialName("description")
        val description: String? = null,
        @SerialName("id")
        val id: Int? = null,
        @SerialName("image_url")
        val imageUrl: String? = null,
        @SerialName("sort")
        val sort: Int? = null,
        @SerialName("status")
        val status: Status? = null,
        @SerialName("title")
        val title: String? = null,
        @SerialName("updated_at")
        val updatedAt: String? = null,
    ) {
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
    }
    @Serializable
    data class Paginator(
        @SerialName("current_page")
        val currentPage: Int? = null,
        @SerialName("first_page_url")
        val firstPageUrl: String? = null,
        @SerialName("from")
        val from: Int? = null,
        @SerialName("last_page")
        val lastPage: Int? = null,
        @SerialName("last_page_url")
        val lastPageUrl: String? = null,
        @SerialName("links")
        val links: List<Link>? = null,
        @SerialName("next_page_url")
        val nextPageUrl: String? = null,
        @SerialName("path")
        val path: String? = null,
        @SerialName("per_page")
        val perPage: Int? = null,
        @SerialName("prev_page_url")
        val prevPageUrl: String? = null,
        @SerialName("to")
        val to: Int? = null,
        @SerialName("total")
        val total: Int? = null,
    ) {
        @Serializable
        data class Link(
            @SerialName("active")
            val active: Boolean? = null,
            @SerialName("label")
            val label: String? = null,
            @SerialName("url")
            val url: String? = null,
        )
    }
}

fun List<BannersResponse.Banner>.toBanners() = this.map { it.toBanners() }


fun BannersResponse.Banner.toBanners() = Banners(
    id = id ?: 0,
    image = imageUrl ?: "",
    description = description ?: "",
    title = title ?: "",
)