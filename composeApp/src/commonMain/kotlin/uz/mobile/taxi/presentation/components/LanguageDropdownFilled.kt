package uz.mobile.taxi.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import taxi.composeapp.generated.resources.Res
import taxi.composeapp.generated.resources.ic_chevron_down
import org.jetbrains.compose.resources.painterResource
import uz.mobile.taxi.presentation.theme.getTypography
import uz.mobile.taxi.presentation.theme.mainButtonTextColorDark

@Composable
fun LanguageDropdownFilled(
    onLanguageSelected: (String) -> Unit,
    selectedLanguage: String,
    modifier: Modifier = Modifier
) {


    val isDropDownExpanded = remember { mutableStateOf(false) }

    val languages = listOf("Uz", "Ru")

    val selectedIndex = languages.indexOfFirst { it.equals(selectedLanguage, ignoreCase = true) }
    var itemPosition by rememberSaveable { mutableStateOf(if (selectedIndex >= 0) selectedIndex else 0) }


    Box(
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(32.dp))
                .clickable {
                    isDropDownExpanded.value = true
                }.padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Text(
                text = languages[itemPosition],
                style = getTypography().labelSmall.copy(color = mainButtonTextColorDark)
            )

            Spacer(Modifier.width(4.dp))

            Icon(
                painter = painterResource(Res.drawable.ic_chevron_down),
                contentDescription = "DropDown Icon",
                tint = mainButtonTextColorDark
            )
        }

        DropdownMenu(
            modifier = Modifier.background(Color.White),
            expanded = isDropDownExpanded.value,
            onDismissRequest = {
                isDropDownExpanded.value = false
            }) {
            languages.forEachIndexed { index, language ->
                DropdownMenuItem(text = {
                    Text(
                        text = language,
                        style = getTypography().labelSmall.copy(color = mainButtonTextColorDark)
                    )
                }, onClick = {
                    isDropDownExpanded.value = false
                    itemPosition = index
                    onLanguageSelected.invoke(languages[index])
                })
            }
        }
    }
}