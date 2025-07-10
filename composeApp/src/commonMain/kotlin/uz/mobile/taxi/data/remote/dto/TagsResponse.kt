package uz.mobile.taxi.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import uz.mobile.taxi.domain.model.Tag

@Serializable
data class TagsResponse(
    @SerialName("tags")
    val tags: List<TagDTO>? = null,
)

@Serializable
data class TagDTO(
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("slug")
    val slug: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null
)


fun List<TagDTO>.toTags() = this.map { it.toTag() }


fun TagDTO.toTag() = Tag(
    id = id,
    name = name,
    slug = slug,
    createdAt = createdAt,
    updatedAt = updatedAt
)

