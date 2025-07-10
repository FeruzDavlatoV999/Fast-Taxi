package uz.mobile.taxi.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import taxi.composeapp.generated.resources.Res
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import taxi.composeapp.generated.resources.exit
import taxi.composeapp.generated.resources.no
import taxi.composeapp.generated.resources.yes
import org.jetbrains.compose.resources.stringResource
import uz.mobile.taxi.presentation.theme.balanceErrorBackgroundColor
import uz.mobile.taxi.presentation.theme.cancelButtonBorderColor
import uz.mobile.taxi.presentation.theme.getTypography
import uz.mobile.taxi.presentation.theme.primaryLight

@Composable
fun LogOutDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(

        onDismissRequest = { onDismiss() },
        title = {
            Text(text = stringResource(Res.string.exit), color = Color.White, fontSize = 14.sp)
        },

        dismissButton = {
            Button(
                modifier = Modifier.width(110.dp),
                onClick = { onDismiss() },
                colors = ButtonDefaults.buttonColors(
                    Color(0xFF2C2C2C),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = stringResource(Res.string.no))
            }
        },
        confirmButton = {
            Button(
                modifier = Modifier.width(110.dp),
                onClick = { onConfirm() },
                colors = ButtonDefaults.buttonColors(
                    Color(0xFF6BF1F6),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = stringResource(Res.string.yes), color = Color.Black)
            }
        },
        containerColor = Color(0xFF121212),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.width(380.dp)
    )
}

@Composable
fun CustomLogOutDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Box(
            modifier = Modifier
                .background(Color(0xFF202123), shape = RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .width(300.dp)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = stringResource(Res.string.exit),
                    style = getTypography().bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp)
                            .padding(end = 8.dp)
                            .background(color = primaryLight, shape = RoundedCornerShape(8.dp))
                            .clickable(onClick = onConfirm),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(Res.string.yes),
                            style = getTypography().titleMedium.copy(
                                color = balanceErrorBackgroundColor,
                                fontWeight = FontWeight(700)
                            )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp)
                            .padding(end = 8.dp)
                            .border(
                                width = 1.dp,
                                color = cancelButtonBorderColor,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable(onClick = onDismiss),
                        contentAlignment = Alignment.Center

                    ) {
                        Text(
                            text = stringResource(Res.string.no),
                            style = getTypography().titleMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight(700)
                            )
                        )
                    }
                }
            }
        }
    }
}
