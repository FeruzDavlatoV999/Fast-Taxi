package uz.mobile.joybox.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.mobile.joybox.data.remote.dto.BannersResponse

@Composable
fun PagerMovieCard(
    modifier: Modifier = Modifier,
    movie: BannersResponse.Banner,
) {

    Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {
        Box {
            CachedAsyncImage(
                url = movie.imageUrl ?: "",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier =
                Modifier.fillMaxSize()
                    .background(
                        brush =
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0xFF0F1111)),
                            startY = 0.4f * 280.dp.value
                        ),
                    ),
                contentAlignment = Alignment.BottomStart
            ) {
            }
        }

        Box(
            modifier = Modifier,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = movie.title ?: "",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xFF2A2B2B))
                            .size(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (movie.sort ?: 0).toString(),
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }

                    Text(text = movie.createdAt ?: "", color = Color.White)
//                    Text(text = movie.duration, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}



