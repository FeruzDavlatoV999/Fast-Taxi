package uz.mobile.taxi.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import taxi.composeapp.generated.resources.Res
import taxi.composeapp.generated.resources.inter_medium
import org.jetbrains.compose.resources.Font
import uz.mobile.taxi.data.remote.dto.MovieDetailResponse
import uz.mobile.taxi.domain.model.Movie

@Composable
fun MovieCardSmall(movie: MovieDetailResponse?, onClick: () -> Unit) {
    val rememberedClick = remember {
        Modifier.clickable {
            onClick()
        }
    }
    Card(
        modifier = Modifier
            .size(width = 155.dp, height = 228.dp)
            .then(rememberedClick)
            .border(0.2.dp, Color.LightGray, RoundedCornerShape(12.dp))
            .animateContentSize(),
        shape = RoundedCornerShape(8.dp),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            CachedAsyncImage(
                url = movie?.thumbnail ?: "",
                modifier = Modifier.fillMaxSize(),
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0xFF0F1111)),
                            startY = 0f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.BottomStart)
                    .padding(8.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    overflow = TextOverflow.Ellipsis,
                    text = movie?.title ?: "",
                    maxLines = 1,
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 22.4.sp,
                        fontFamily = FontFamily(
                            Font(
                                Res.font.inter_medium,
                                FontWeight.Normal,
                                FontStyle.Normal
                            )
                        ),
                        fontWeight = FontWeight(600),
                        color = Color(0xFFFFFFFF),
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(4.dp))

                val year = if (movie == null) " • " + movie?.publishAt else ""

                val name = movie?.genres
                    ?.mapNotNull { it.name?.takeIf { name -> name.isNotBlank() } }
                    ?.filter { it.isNotEmpty() && it.isNotBlank() }
                    ?.joinToString(separator = " • ")
                println("@@@ name genre ${name}")
                Text(
                    text = "$name $year",
                    color = Color.White,
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

    }
}

@Composable
fun MovieCardSmall(movie: Movie, onClick: () -> Unit) {
    val rememberedClick = remember {
        Modifier.clickable {
            onClick()
        }
    }

    Card(
        modifier = Modifier
            .size(width = 155.dp, height = 228.dp)
            .then(rememberedClick)
            .border(0.2.dp, Color.LightGray, RoundedCornerShape(12.dp))
            .animateContentSize(),
        shape = RoundedCornerShape(8.dp),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            CachedAsyncImage(
                url = movie.image,
                modifier = Modifier.fillMaxSize(),
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0xFF0F1111)),
                            startY = 0f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.BottomStart)
                    .padding(8.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = movie.title ?: "",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${movie?.genres
                        ?.mapNotNull { it.name?.takeIf { name -> name.isNotBlank() } } // Filter out null or blank names
                        ?.filter { it.isNotEmpty() } // Extra filter to make sure no empty names
                        ?.joinToString(separator = " • ") // Join valid names with separator
                    }",
                    color = Color.White,
                    fontSize = 14.sp,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

    }
}

