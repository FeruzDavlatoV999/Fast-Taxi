package uz.mobile.taxi.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import taxi.composeapp.generated.resources.Res
import taxi.composeapp.generated.resources.ic_network
import taxi.composeapp.generated.resources.inter_medium
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource

@Composable
fun InternetOffline(
    tryAgain: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(Res.drawable.ic_network),
            contentDescription = null,
            modifier = Modifier
                .width(101.39999.dp)
                .height(119.6.dp),
            contentScale = ContentScale.Fit
        )

        Text(modifier = Modifier.padding(start = 0.dp, top = 8.dp, end = 0.dp, bottom = 16.dp),
            text = "одключение к интернету отсутствует",
            style = TextStyle(
                fontSize = 14.sp,
                lineHeight = 21.sp,
                fontFamily = FontFamily(Font(Res.font.inter_medium)),
                fontWeight = FontWeight(500),
                color = Color(0xFFFFFFFF),
            )
        )

        MainButton("Повторить", modifier = Modifier.padding(horizontal = 32.dp)){
            tryAgain()
        }

    }
}