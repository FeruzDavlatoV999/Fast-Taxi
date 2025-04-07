package uz.mobile.joybox.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import joybox.composeapp.generated.resources.Res
import joybox.composeapp.generated.resources.ic_chevron_down
import org.jetbrains.compose.resources.vectorResource
import uz.mobile.joybox.presentation.theme.disableButtonColor
import uz.mobile.joybox.presentation.theme.getTypography
import uz.mobile.joybox.presentation.theme.headlineMediumTextColorDark
import uz.mobile.joybox.presentation.theme.hintTextColorDark
import uz.mobile.joybox.presentation.theme.mainButtonTextColorLight
import uz.mobile.joybox.presentation.theme.textFieldBorderColorDark

@Composable
fun CheckEditableField(
    gender: String,
    isEditable: Boolean,
    isIcon: Boolean,
    onGenderClick: () -> Unit
) {

    Surface(
        modifier = Modifier.fillMaxWidth().height(45.dp).border(
            width = 1.dp,
            color = if (isEditable)headlineMediumTextColorDark  else textFieldBorderColorDark,
            shape = RoundedCornerShape(8.dp)
        ).clickable {
            if (isEditable) onGenderClick()
        },
        shape = RoundedCornerShape(8.dp),
        color = if (isEditable) mainButtonTextColorLight else disableButtonColor,
    ) {
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = gender,
                    style = getTypography().bodyMedium.copy(color = if (isEditable) headlineMediumTextColorDark else hintTextColorDark)
                )
                if (isIcon) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_chevron_down),
                        contentDescription = null,
                        tint = if (isEditable) headlineMediumTextColorDark else textFieldBorderColorDark,
                        modifier = Modifier.size(24.dp)
                    )
                }

            }
        }
    }
}


