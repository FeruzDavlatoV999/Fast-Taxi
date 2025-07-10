package uz.mobile.taxi.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
        val links: List<Link?>? = null,
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