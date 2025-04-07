package uz.mobile.joybox.presentation.components

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.mobile.joybox.domain.util.UniversalText
import uz.mobile.joybox.presentation.theme.disableButtonColor
import uz.mobile.joybox.presentation.theme.errorDark
import uz.mobile.joybox.presentation.theme.getTypography
import uz.mobile.joybox.presentation.theme.headlineMediumTextColorDark
import uz.mobile.joybox.presentation.theme.hintTextColorDark
import uz.mobile.joybox.presentation.theme.mainButtonTextColorDark
import uz.mobile.joybox.presentation.theme.textFieldBorderColorDark


//@Composable
//fun ProfileUserFieldsEditText(
//    value: String,
//    isEditable: Boolean,
//    onValueChange: (String) -> Unit,
//    keyboardType: KeyboardType = KeyboardType.Text
//) {
//    OutlinedTextField(
//        value = value,
//        onValueChange = { onValueChange(it) },
//        modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
//        enabled = isEditable,
//        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
//        textStyle = TextStyle(
//            color = if (isEditable) Color.White else Color(0xFF747E83),
//            fontSize = 16.sp,
//            lineHeight = 24.sp,
//        ),
//        shape = MaterialTheme.shapes.small.copy(CornerSize(16.dp)),
//        colors = TextFieldDefaults.outlinedTextFieldColors(
//            backgroundColor = if (isEditable) Color(0xFF191B1C) else Color(0xFF272B2C),
//            focusedBorderColor = Color.White,
//            unfocusedBorderColor = Color.White,
//            disabledBorderColor = Color(0xFF656E72),
//            textColor = if (isEditable) Color.White else Color(0xFF747E83),
//            disabledTextColor = Color(0xFF747E83),
//            cursorColor = Color.White
//        ),
//        singleLine = true,
//    )
//}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileUserFieldsEditText(
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
            modifier = Modifier.fillMaxWidth().height(45.dp).border(
                width = 1.dp,
                color = if (enabled) headlineMediumTextColorDark else textFieldBorderColorDark,
                shape = RoundedCornerShape(size = 8.dp)
            ),
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
            enabled = enabled,
            singleLine = singleLine,
            textStyle = getTypography().bodyMedium.copy(color = if (enabled) headlineMediumTextColorDark else hintTextColorDark),
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
                    backgroundColor = if (enabled) mainButtonTextColorDark else disableButtonColor,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = headlineMediumTextColorDark,
                    textColor = if (enabled) headlineMediumTextColorDark else hintTextColorDark,
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

