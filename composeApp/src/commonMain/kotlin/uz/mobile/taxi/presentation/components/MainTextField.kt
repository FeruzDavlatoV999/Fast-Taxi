package uz.mobile.taxi.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.mobile.taxi.domain.util.UniversalText
import taxi.composeapp.generated.resources.Res
import taxi.composeapp.generated.resources.inter_bold
import uz.mobile.taxi.presentation.theme.errorDark
import uz.mobile.taxi.presentation.theme.getTypography
import uz.mobile.taxi.presentation.theme.headlineMediumTextColorDark
import uz.mobile.taxi.presentation.theme.hintTextColorDark
import uz.mobile.taxi.presentation.theme.textFieldBorderColorDark


@Composable
fun MainTextField(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    value: String = "",
    hint: String = "",
    error: String = ""
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            shape = RoundedCornerShape(percent = 20),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = Color.Gray
            ),
            modifier = modifier.fillMaxWidth().border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(size = 8.dp)),
            textStyle = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily(org.jetbrains.compose.resources.Font(Res.font.inter_bold, FontWeight.Normal, FontStyle.Normal)),
                fontWeight = FontWeight(400),
                color = Color.White,
            ),
            placeholder = {
                Text(
                    text = hint,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(org.jetbrains.compose.resources.Font(Res.font.inter_bold, FontWeight.Normal, FontStyle.Normal)),
                        fontWeight = FontWeight(400),
                        lineHeight = 16.sp,
                        color = hintTextColorDark,
                    )
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = error,
            style = TextStyle(
                fontSize = 14.sp,
                lineHeight = 16.sp,
                fontFamily = FontFamily(org.jetbrains.compose.resources.Font(Res.font.inter_bold, FontWeight.Normal, FontStyle.Normal)),
                fontWeight = FontWeight(400),
                color = errorDark,
            )
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun InputTextField(
    value: String,
    error: UniversalText = UniversalText.Empty,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
) {
    val interactionSource = remember { MutableInteractionSource() }


    Column(
        modifier = modifier
    ) {

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().height(45.dp).border(width = 1.dp, color = textFieldBorderColorDark, shape = RoundedCornerShape(size = 8.dp)),
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
            enabled = enabled,
            singleLine = singleLine,
            textStyle = getTypography().bodyMedium.copy(headlineMediumTextColorDark),
            keyboardOptions = keyboardOptions,
            cursorBrush = SolidColor(headlineMediumTextColorDark)
        ) { innerTextField ->

            TextFieldDefaults.OutlinedTextFieldDecorationBox(
                value = value,
                visualTransformation = visualTransformation,
                innerTextField = innerTextField,
                singleLine = singleLine,
                enabled = enabled,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = headlineMediumTextColorDark,
                    textColor = headlineMediumTextColorDark
                ),
                interactionSource = interactionSource,
                contentPadding = PaddingValues(horizontal = 14.dp),
                trailingIcon = trailingIcon,
                placeholder = placeholder,
                leadingIcon = leadingIcon
            )
        }

        if (error !is UniversalText.Empty) {
            Spacer(Modifier.height(4.dp))

            Text(
                text = error.asString(),
                style = getTypography().bodyMedium.copy(lineHeight = 21.sp, color = errorDark)
            )
        }

    }
}

