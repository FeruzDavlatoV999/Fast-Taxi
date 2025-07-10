package uz.mobile.taxi.presentation.screens.profile.userComponent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
import taxi.composeapp.generated.resources.*
import org.jetbrains.compose.resources.Font
import uz.mobile.taxi.presentation.components.ProfileUserAvatar

@Composable
fun ExpandableProfileHeader(
    name: String,
    email: String,
    id: String,
    url: String,
    onClick: () -> Unit,
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp)
                .clickable {
                    onClick()
                }
                .padding(horizontal = 8.dp, vertical = 44.dp)
        ) {
            ProfileUserAvatar(url,null, name, false, onEditClick = {

            })

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,

                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(
                                Res.font.inter_bold,
                                FontWeight.Normal,
                                FontStyle.Normal
                            )
                        ),
                        fontWeight = FontWeight(700),
                        color = Color(0xFFEEEFF0),
                    )
                )
                Text(
                    text = email,
                    color = Color(0xFFB9BFC1),
                    fontSize = 16.sp,
                    style = TextStyle(
                        fontFamily = FontFamily(
                            Font(
                                Res.font.inter_light,
                                FontWeight.Normal,
                                FontStyle.Normal
                            )
                        ),
                        fontWeight = FontWeight(400)
                    )
                )

                Text(
                    text = "ID: $id",
                    color = Color(0xFFB9BFC1),
                    fontSize = 16.sp,
                    style = TextStyle(
                        fontFamily = FontFamily(
                            Font(
                                Res.font.inter_light,
                                FontWeight.Normal,
                                FontStyle.Normal
                            )
                        ),
                        fontWeight = FontWeight(400)
                    )
                )
            }

            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}