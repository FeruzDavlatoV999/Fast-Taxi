package uz.mobile.taxi.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import uz.mobile.taxi.presentation.theme.getTypography
import uz.mobile.taxi.presentation.theme.mainButtonTextColorDark
import uz.mobile.taxi.presentation.theme.textFieldBorderColorDark

@Composable
fun ProfileUserAvatar(
    imageUri: String?,
    imageBitmap: ImageBitmap?,
    userName: String,
    isEdit: Boolean,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.size(92.dp),
        contentAlignment = Alignment.TopEnd,
    ) {
        when {
            imageBitmap != null -> {
                Image(
                    bitmap = imageBitmap,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(92.dp)
                        .clip(CircleShape)
                        .background(Color.Gray, shape = CircleShape),
                )
            }

            !imageUri.isNullOrEmpty() -> {
                AsyncImage(
                    model = imageUri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(92.dp)
                        .clip(CircleShape)
                        .background(Color.Gray, shape = CircleShape),
                    onError = {
                        println("Error loading image: $imageUri")
                    }
                )
            }

            else -> {
                Box(
                    modifier = Modifier.size(92.dp)
                        .background(color = mainButtonTextColorDark, shape = CircleShape)
                        .clip(CircleShape)
                        .border(
                            width = 1.5.dp,
                            shape = CircleShape,
                            color = textFieldBorderColorDark
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userName.firstOrNull()?.toString()?.uppercase() ?: "M",
                        style = getTypography().headlineMedium
                    )
                }
            }
        }

        if (isEdit) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(Color.White, shape = CircleShape)
                    .border(width = 2.dp, color = Color.Black, shape = CircleShape)
                    .padding(4.dp)
                    .clickable { onEditClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = Color.Black,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}



