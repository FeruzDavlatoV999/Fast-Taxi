package uz.mobile.taxi.presentation.components

import androidx.compose.foundation.background
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
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.mobile.taxi.domain.util.UniversalText
import uz.mobile.taxi.presentation.theme.errorDark
import uz.mobile.taxi.presentation.theme.getTypography
import uz.mobile.taxi.presentation.theme.headlineMediumTextColorDark
import uz.mobile.taxi.presentation.theme.hintTextColorDark
import uz.mobile.taxi.presentation.theme.textFieldBorderColorDark


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PasswordTextField(
    value: String,
    hint: String = "",
    error: UniversalText = UniversalText.Empty,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    var showPassword by remember { mutableStateOf(value = false) }


    Column {

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier.fillMaxWidth().border(width = 1.dp, color = textFieldBorderColorDark, shape = RoundedCornerShape(size = 8.dp)).background(Color(0xFF191B1C)),
            visualTransformation = if (showPassword) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            interactionSource = interactionSource,
            enabled = enabled,
            singleLine = singleLine,
            textStyle = getTypography().bodyMedium.copy(headlineMediumTextColorDark),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            cursorBrush = SolidColor(headlineMediumTextColorDark)
        ) { innerTextField ->

            TextFieldDefaults.OutlinedTextFieldDecorationBox(
                value = value,
                visualTransformation = VisualTransformation.None,
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
                trailingIcon = {
                    if (showPassword) {
                        IconButton(onClick = { showPassword = false }) {
                            Icon(
                                imageVector = Icons.Outlined.Visibility,
                                contentDescription = "hide_password",
                                tint = textFieldBorderColorDark
                            )
                        }
                    } else {
                        IconButton(
                            onClick = { showPassword = true }) {
                            Icon(
                                imageVector = Icons.Outlined.VisibilityOff,
                                contentDescription = "hide_password",
                                tint = textFieldBorderColorDark
                            )
                        }
                    }
                },
                placeholder = {
                    Text(
                        text = hint,
                        style = getTypography().bodyMedium.copy(hintTextColorDark)
                    )
                },
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


