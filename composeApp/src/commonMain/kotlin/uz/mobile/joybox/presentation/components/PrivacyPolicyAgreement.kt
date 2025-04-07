package uz.mobile.joybox.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import joybox.composeapp.generated.resources.Res
import joybox.composeapp.generated.resources.privacy_policy_part1
import joybox.composeapp.generated.resources.privacy_policy_part2
import joybox.composeapp.generated.resources.privacy_policy_part3
import joybox.composeapp.generated.resources.privacy_policy_part4
import org.jetbrains.compose.resources.stringResource
import uz.mobile.joybox.presentation.theme.getTypography
import uz.mobile.joybox.presentation.theme.primaryDark
import uz.mobile.joybox.presentation.theme.textFieldBorderColorDark
import uz.mobile.joybox.presentation.theme.textGrey60ColorDark

@Composable
fun PrivacyPolicyAgreement(
    isChecked: Boolean = true,
    onCheckedChange: (Boolean) -> Unit,
    onClick: () -> Unit
) {
    val textColor = if (isChecked) Color.White else Color.Gray

    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.size(20.dp),
            colors = CheckboxDefaults.colors(
                checkedColor = primaryDark,
                uncheckedColor = textFieldBorderColorDark
            ),

            )
        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = textGrey60ColorDark)) {
                    append(stringResource(Res.string.privacy_policy_part1))
                }
                append(" ")
                withStyle(style = SpanStyle(color = primaryDark)) {
                    append(stringResource(Res.string.privacy_policy_part2))
                }
                append(" ")
                withStyle(style = SpanStyle(color = textGrey60ColorDark)) {
                    append(stringResource(Res.string.privacy_policy_part3))
                }
                append(" ")
                withStyle(style = SpanStyle(color = primaryDark)) {
                    append(stringResource(Res.string.privacy_policy_part4))
                }
            },
            color = textColor,
            style = getTypography().bodyLarge.copy(fontSize = 12.sp, color = textGrey60ColorDark),
            modifier = Modifier.clickable(onClick = onClick)
        )
    }
}
