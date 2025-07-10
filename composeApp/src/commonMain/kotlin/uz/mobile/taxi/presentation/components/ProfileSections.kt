package uz.mobile.taxi.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import taxi.composeapp.generated.resources.Res
import taxi.composeapp.generated.resources.inter_medium
import org.jetbrains.compose.resources.Font

@Composable
fun ProfileSections(title: String, isIconVisible: Boolean,isIconLock: Boolean = false,onClick: () -> Unit) {

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onClick()
                }
                .padding(0.dp,16.dp,8.dp,16.dp)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f).padding(start = 16.dp),
                style = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp,
                    lineHeight = 29.sp,
                    fontFamily = FontFamily(Font(Res.font.inter_medium, FontWeight.Normal, FontStyle.Normal)),
                    fontWeight = FontWeight(500)
                )
            )
            if (isIconVisible && !isIconLock) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            if (isIconLock){
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }

        Divider(color = Color.Gray.copy(alpha = 0.5f), thickness = 0.5.dp)
    }
}