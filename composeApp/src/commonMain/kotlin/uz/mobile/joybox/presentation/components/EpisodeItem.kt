package uz.mobile.joybox.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import joybox.composeapp.generated.resources.Res
import joybox.composeapp.generated.resources.ic_pause_video
import joybox.composeapp.generated.resources.ic_play_small
import org.jetbrains.compose.resources.painterResource
import uz.mobile.joybox.domain.model.Movie
import uz.mobile.joybox.presentation.theme.getTypography

@Composable
fun EpisodeItem(
    movie: Movie,
    isSelected: Boolean,
    onPlayClick: (Movie) -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp)).fillMaxWidth()
            .clickable { onPlayClick(movie) }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                CachedAsyncImage(
                    url = movie.image,
                    modifier = Modifier.fillMaxWidth().height(104.dp).clip(RoundedCornerShape(10.dp)),
                )

                Icon(
                    painter = if (isSelected) painterResource(Res.drawable.ic_pause_video) else painterResource(Res.drawable.ic_play_small),
                    contentDescription = "Play",
                    modifier = Modifier.align(Alignment.Center).size(20.dp),
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = movie.title,
                style = getTypography().displayMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = movie.description,
                maxLines = 1,
                textAlign = TextAlign.Start,
                style = getTypography().displaySmall,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}
