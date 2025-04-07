package uz.mobile.joybox.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.mobile.joybox.domain.util.UniversalText
import uz.mobile.joybox.presentation.theme.errorDark
import uz.mobile.joybox.presentation.theme.getTypography
import uz.mobile.joybox.presentation.theme.headlineMediumTextColorDark
import uz.mobile.joybox.presentation.theme.textFieldBorderColorDark


sealed interface PhoneNumberElement {
    data class Mask(val text: String) : PhoneNumberElement
    data object EditableDigit : PhoneNumberElement

    object FormatPatterns {
        val UZB by lazy {
            listOf(
                Mask("+998"),
                Mask(" ("),
                EditableDigit,
                EditableDigit,
                Mask(") "),
                EditableDigit,
                EditableDigit,
                EditableDigit,
                Mask("-"),
                EditableDigit,
                EditableDigit,
                Mask("-"),
                EditableDigit,
                EditableDigit,
            )
        }
    }
}


@Composable
fun PhoneInputTextField(
    modifier: Modifier = Modifier,
    config: List<PhoneNumberElement>,
    onValueChange: (String) -> Unit,
    value: String = "",
    error: UniversalText = UniversalText.Empty
) {
    val editableDigitCount = remember {
        config.filterIsInstance<PhoneNumberElement.EditableDigit>()
    }


    Column {
        BasicTextField(
            modifier = modifier
                .fillMaxWidth()
                .height(45.dp)
                .border(width = 1.dp, color = textFieldBorderColorDark, shape = RoundedCornerShape(size = 8.dp))
                .background(color = Color(0xFF191B1C), shape = RoundedCornerShape(size = 8.dp))
                .padding(horizontal = 16.dp, vertical = 14.dp),
            value = TextFieldValue(
                text = value,
                selection = TextRange(value.length)
            ),

            cursorBrush = SolidColor(headlineMediumTextColorDark),
            onValueChange = { newValue ->
                if (newValue.text.length <= editableDigitCount.size) {
                    onValueChange(newValue.text)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
            ),
            decorationBox = {
                val digitIndexMap = remember(value) {
                    var start = 0
                    config.mapIndexedNotNull { index, it ->
                        if (it is PhoneNumberElement.EditableDigit) {
                            index to start++
                        } else null
                    }.toMap()
                }
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    config.forEachIndexed { index, phoneNumberElement ->
                        when (phoneNumberElement) {
                            PhoneNumberElement.EditableDigit -> {
                                digitIndexMap[index]?.let { digitIndex ->
                                    EditableDigit(
                                        text = value.getOrNull(digitIndex)?.toString(),
                                    )
                                }
                            }

                            is PhoneNumberElement.Mask -> {
                                Mask(
                                    text = phoneNumberElement.text,
                                )
                            }
                        }
                    }
                }
            }
        )

        if (error !is UniversalText.Empty) {
            Spacer(Modifier.height(4.dp))

            Text(
                text = error.asString(),
                style = getTypography().bodyMedium.copy(lineHeight = 21.sp, color = errorDark)
            )
        }
    }
}


@Composable
fun EditableDigit(
    modifier: Modifier = Modifier,
    text: String?,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AnimatedContent(
            targetState = text,
            transitionSpec = {
                (slideInVertically() + fadeIn()).togetherWith(slideOutVertically() + fadeOut())
            }) { currentText ->
            Text(
                modifier = Modifier.wrapContentSize(unbounded = true),
                text = currentText ?: "_",
                style = getTypography().bodyMedium.copy(color = headlineMediumTextColorDark),
            )
        }
    }
}

@Composable
fun Mask(
    modifier: Modifier = Modifier,
    text: String,
) {
    Text(
        modifier = modifier.wrapContentSize(unbounded = true),
        text = text,
        style = getTypography().bodyMedium.copy(color = headlineMediumTextColorDark),
    )
}
