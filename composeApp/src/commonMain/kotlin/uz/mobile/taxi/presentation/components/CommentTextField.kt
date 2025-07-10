package uz.mobile.taxi.presentation.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import taxi.composeapp.generated.resources.Res
import taxi.composeapp.generated.resources.write_comment
import org.jetbrains.compose.resources.stringResource
import uz.mobile.taxi.presentation.theme.commentFieldBackgroundColor
import uz.mobile.taxi.presentation.theme.getTypography


@Composable
fun CommentTextField(
    comment: String,
    onCommentChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        modifier = modifier,
        shape = RoundedCornerShape(percent = 5),
        colors = TextFieldDefaults.colors().copy(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedContainerColor = commentFieldBackgroundColor,
            unfocusedContainerColor = commentFieldBackgroundColor
        ),
        textStyle = getTypography().bodyLarge,
        value = comment,
        onValueChange = onCommentChanged,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        placeholder = {
            Text(
                text = stringResource(Res.string.write_comment),
                style = getTypography().bodyMedium.copy(
                    lineHeight = 24.sp,
                    fontSize = 16.sp,
                    color = Color(0x99FFFFFF)
                )
            )
        }
    )
}