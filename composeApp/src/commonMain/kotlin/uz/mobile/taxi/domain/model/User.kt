package uz.mobile.taxi.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val firstname: String? = null,
    val phone: String? = null,
    val avatar: String? = null,
    val password: String? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val email: String? = null,
    val gender: String? = null,
    val birthday: String? = null,
    val userId: String? = null
) : JavaSerializable
