package uz.mobile.taxi.presentation.screens.comments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.cash.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import taxi.composeapp.generated.resources.Res
import taxi.composeapp.generated.resources.comments
import taxi.composeapp.generated.resources.ic_vector_star
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.core.parameter.parametersOf
import uz.mobile.taxi.domain.model.Comment
import uz.mobile.taxi.presentation.components.CachedAsyncImage
import uz.mobile.taxi.presentation.components.MainTopBar
import uz.mobile.taxi.presentation.platform.MoviesAppScreen
import uz.mobile.taxi.presentation.theme.commentCardBackgroundColor
import uz.mobile.taxi.presentation.theme.commentTextColor
import uz.mobile.taxi.presentation.theme.getTypography
import uz.mobile.taxi.presentation.theme.reviewStarColor
import uz.mobile.taxi.presentation.util.getNavigationBarHeight


class CommentsScreen(
    private val movieId: Int
) : MoviesAppScreen {

    @Composable
    override fun Content() {
        CommentsScreenContent()
    }

    @Composable
    fun CommentsScreenContent(
        viewModel: CommentsScreenModel = koinScreenModel { parametersOf(movieId) },
        navigator: Navigator = LocalNavigator.currentOrThrow
    ) {

        val comments = viewModel.commentsFlow.collectAsLazyPagingItems()

        Column(
            modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            MainTopBar(text = stringResource(Res.string.comments)) {
                navigator.pop()
            }

            Spacer(modifier = Modifier.height(10.dp))

            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {

                if (viewModel.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(alignment = Alignment.Center),
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 20.dp)
                ) {
                    items(comments.itemCount) { index ->
                        comments[index]?.let { comment ->
                            ReviewCard(comment = comment)
                            Spacer(Modifier.height(10.dp))
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height((75 + getNavigationBarHeight()).dp))
                    }
                }
            }

        }
    }

}

@Composable
fun ReviewCard(
    comment: Comment
) {
    Column(
        modifier = Modifier
            .background(commentCardBackgroundColor, RoundedCornerShape(6.dp))
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            CachedAsyncImage(
                url = comment.userPhoto,
                modifier = Modifier.size(25.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = comment.userName,
                style = getTypography().titleSmall.copy(color = Color.White)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = comment.comment,
            style = getTypography().displaySmall.copy(color = commentTextColor)
        )

        Spacer(modifier = Modifier.height(16.dp))


        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {

            Row {
                val rating = comment.rating.toInt()
                repeat(rating) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_vector_star),
                        contentDescription = "Star",
                        tint = reviewStarColor,
                        modifier = Modifier.size(14.dp).padding(2.dp)
                    )
                }
            }

            Text(
                text = comment.date,
                color = Color(0xFFA9AAAC),
                fontSize = 9.sp,
                lineHeight = 11.sp,
                fontWeight = FontWeight.Normal,
            )
        }
    }
}