package uz.mobile.taxi.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import taxi.composeapp.generated.resources.Res
import taxi.composeapp.generated.resources.notifications
import org.jetbrains.compose.resources.stringResource

@Composable
fun NotificationToggle(
    isChecked:Boolean,
    isClicked: (Boolean) -> Unit,
) {


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp,8.dp,16.dp,8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = stringResource(Res.string.notifications),
            fontSize = 16.sp,
            color = Color.White
        )

        Switch(
            checked = isChecked,
            onCheckedChange = { isClicked(it) },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color.Cyan, // Change the track color
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.Gray
            )
        )
    }
}