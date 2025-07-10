package uz.mobile.taxi.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import taxi.composeapp.generated.resources.Res
import taxi.composeapp.generated.resources.inter_light
import taxi.composeapp.generated.resources.inter_medium
import taxi.composeapp.generated.resources.tv_logo_profile
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource

@Composable
fun ReviewDialog(
    onDismiss: () -> Unit,
    onSubmitRating: (Float) -> Unit,
    selectedRating: Float,
) {

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color.LightGray
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MakonTvLogo()

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Review Store Review Controller?",
                    modifier = Modifier.padding(horizontal = 32.dp),
                    style = TextStyle(
                        fontSize = 18.sp,
                        lineHeight = 22.sp,
                        fontFamily = FontFamily(Font(Res.font.inter_medium, FontWeight.Normal, FontStyle.Normal)),
                        fontWeight = FontWeight(600),
                        color = Color(0xFF000000),
                        textAlign = TextAlign.Center,
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Tap a star to rate it on the App Store.",
                    modifier = Modifier.padding(horizontal = 58.dp),
                    style = TextStyle(
                        fontSize = 15.sp,
                        lineHeight = 20.sp,
                        fontFamily = FontFamily(Font(Res.font.inter_light, FontWeight.Normal, FontStyle.Normal)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFF000000),
                        textAlign = TextAlign.Center,
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = Color.Gray.copy(alpha = 0.4f), thickness = 1.dp)
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    StarRatingBar(
                        maxStars = 5,
                        rating = selectedRating,
                        onRatingChanged = {
                            onSubmitRating(it)
                        }
                    )
                }
                Divider(color = Color.Gray.copy(alpha = 0.5f), thickness = 1.dp)

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = { onDismiss() }) {
                    Text(
                        text = "Not now",
                        modifier = Modifier.padding(horizontal = 16.dp),
                        style = TextStyle(
                            fontSize = 17.sp,
                            lineHeight = 22.sp,
                            fontFamily = FontFamily(Font(Res.font.inter_light, FontWeight.Normal, FontStyle.Normal)),
                            fontWeight = FontWeight(400),
                            color = Color(0xFF007AFF),
                            textAlign = TextAlign.Center,
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun StarRatingBar(
    maxStars: Int = 5,
    rating: Float,
    onRatingChanged: (Float) -> Unit,
) {
    val density = LocalDensity.current.density
    val starSize = (12f * density).dp
    val starSpacing = (0.5f * density).dp

    Row(
        modifier = Modifier.selectableGroup(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..maxStars) {
            val isSelected = i <= rating
            val icon = if (isSelected) Icons.Filled.Star else Icons.Default.StarBorder
            val iconTintColor = if (isSelected) Color(0xFF007AFF) else Color(0xFF007AFF)
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTintColor,
                modifier = Modifier
                    .selectable(
                        selected = isSelected,
                        onClick = {
                            onRatingChanged(i.toFloat())
                        }
                    )
                    .width(starSize).height(starSize)
            )

            if (i < maxStars) {
                Spacer(modifier = Modifier.width(starSpacing))
            }
        }
    }
}

@Composable
fun MakonTvLogo() {
    Box(
        modifier = Modifier
            .padding(top = 20.dp, bottom = 8.dp)
            .size(60.dp)
            .background(
                color = Color.Black,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(Res.drawable.tv_logo_profile),
                contentDescription = "Makon TV Logo",
                modifier = Modifier.width(38.dp).height(48.dp)
            )
        }
    }
}
