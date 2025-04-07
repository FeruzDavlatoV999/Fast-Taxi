package uz.mobile.joybox.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import uz.mobile.joybox.presentation.theme.errorDark
import uz.mobile.joybox.presentation.theme.headlineMediumTextColorDark
import uz.mobile.joybox.presentation.theme.textFieldBorderColorDark


@Composable
fun OtpTextField(
    modifier: Modifier = Modifier,
    otpText: String,
    otpCount: Int = 5,
    onOtpTextChange: (String, Boolean) -> Unit,
    isIncorrect: Boolean = false
) {

    LaunchedEffect(Unit) {
        if (otpText.length > otpCount) {
            throw IllegalArgumentException("Otp text value must not have more than otpCount: $otpCount characters")
        }
    }


    BasicTextField(
        modifier = modifier,
        value = TextFieldValue(otpText, selection = TextRange(otpText.length)),
        onValueChange = {
            if (it.text.length <= otpCount) {
                onOtpTextChange.invoke(it.text, it.text.length == otpCount)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        decorationBox = {
            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                repeat(otpCount) { index ->
                    CharView(
                        index = index,
                        text = otpText,
                        isError = isIncorrect
                    )

                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        }
    )
}

@Composable
private fun CharView(
    index: Int,
    text: String,
    isError: Boolean
) {
    val isFocused = text.length == index
    val char = when {
        index == text.length -> ""
        index > text.length -> ""
        else -> text[index].toString()
    }
    Box(
        modifier = Modifier
            .width(48.dp)
            .height(48.dp)
            .border(
                1.dp, when {
                    isFocused -> Color.White
                    isError -> errorDark
                    else -> textFieldBorderColorDark
                }, RoundedCornerShape(8.dp)
            )
            .background(Color(0xFF191B1C))
    ) {
        Text(
            text = char,
            modifier = Modifier.align(Alignment.Center),
            color = headlineMediumTextColorDark,
            textAlign = TextAlign.Center
        )
    }
}