package uz.mobile.taxi.domain.util

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

sealed class UniversalText {

    companion object {
        private const val EMPTY = ""
    }

    data class Dynamic(val value: String) : UniversalText()
    class Resource(val resource: StringResource, vararg val args: Any) : UniversalText()
    data object Empty : UniversalText()

    @Composable
    fun asString(): String {
        return when (this) {
            is Dynamic -> value
            is Resource -> stringResource(resource, args)
            Empty -> EMPTY
        }
    }

    fun dynamic(): String {
        return when (this) {
            is Dynamic -> value
            else -> EMPTY
        }
    }

}