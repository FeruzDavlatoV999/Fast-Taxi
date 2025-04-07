package uz.mobile.joybox.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    val id: Int?,
    val name: String?,
    val slug: String?,
    val createdAt: String?,
    val updatedAt: String?,
):JavaSerializable