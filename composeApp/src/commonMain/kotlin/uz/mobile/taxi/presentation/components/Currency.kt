package uz.mobile.taxi.presentation.components

import androidx.compose.runtime.Composable
import taxi.composeapp.generated.resources.Res
import taxi.composeapp.generated.resources.sum
import org.jetbrains.compose.resources.stringResource


@Composable
fun Long?.sum() : String {
    val money = (this ?: 0).toString().reversed().chunked(3).joinToString(" ").reversed().trim()
    val sum = stringResource(Res.string.sum)
    return StringBuilder().append(money).append(" ").append(sum).toString()
}
