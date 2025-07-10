package uz.mobile.taxi.data.remote.dto.base

import kotlinx.serialization.Serializable

@Serializable
class Errors {
    val errors: Map<String, List<String>>? = null
}