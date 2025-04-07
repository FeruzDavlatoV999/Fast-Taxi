package uz.mobile.joybox.presentation.screens.detailsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import chaintech.network.connectivitymonitor.ConnectivityStatus
import joybox.composeapp.generated.resources.Res
import joybox.composeapp.generated.resources.comments
import joybox.composeapp.generated.resources.description
import joybox.composeapp.generated.resources.ic_back
import joybox.composeapp.generated.resources.inter_light
import joybox.composeapp.generated.resources.leave_comment
import joybox.composeapp.generated.resources.play_circle
import joybox.composeapp.generated.resources.see_all
import joybox.composeapp.generated.resources.send
import joybox.composeapp.generated.resources.show
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.core.parameter.parametersOf
import uz.mobile.joybox.data.remote.dto.MovieDetailResponse
import uz.mobile.joybox.data.remote.dto.TagDTO
import uz.mobile.joybox.data.remote.dto.toMovie
import uz.mobile.joybox.presentation.NetworkScreenModel
import uz.mobile.joybox.presentation.components.CachedAsyncImage
import uz.mobile.joybox.presentation.components.CommentTextField
import uz.mobile.joybox.presentation.components.InternetOffline
import uz.mobile.joybox.presentation.components.MainButton
import uz.mobile.joybox.presentation.components.StarRatingCommentBar
import uz.mobile.joybox.presentation.platform.MoviesAppScreen
import uz.mobile.joybox.presentation.screens.comments.CommentsScreen
import uz.mobile.joybox.presentation.screens.comments.ReviewCard
import uz.mobile.joybox.presentation.screens.play.PlayScreen
import uz.mobile.joybox.presentation.screens.pricing.PricingPlanScreen
import uz.mobile.joybox.presentation.theme.getTypography
import uz.mobile.joybox.presentation.theme.headlineMediumTextColorDark
import uz.mobile.joybox.presentation.theme.mainButtonTextColorDark
import uz.mobile.joybox.presentation.util.getNavigationBarHeight

class DetailsScreen(private val movieId: Int, private val title: String) : MoviesAppScreen {

    @Composable
    override fun Content() {
        val networkScreenModel = remember { NetworkScreenModel() }
        val connectivityStatus by networkScreenModel.connectivityStatus.collectAsState()
        networkScreenModel.startMonitoring()
        when (connectivityStatus) {
            ConnectivityStatus.NOT_CONNECTED -> InternetOffline(tryAgain = { networkScreenModel.refresh() })
            else -> {
                MainScreen()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun MainScreen(
        viewModel: DetailsScreenViewModel = koinScreenModel { parametersOf(movieId) },
        navigator: Navigator = LocalNavigator.currentOrThrow,
    ) {

        val detail by viewModel.getMovieDetailInfo.collectAsState()

        Scaffold(
            containerColor = Color(0xFF0F1111), modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black),
            topBar = {
                CenterAlignedTopAppBar(title = {
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.titleSmall
                    )
                }, navigationIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.ic_back),
                        contentDescription = "back",
                        tint = Color.White,
                        modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 0.dp).clickable {
                            navigator.pop()
                        }
                    )
                },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color(0xFF0F1111),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            }) { padding ->

            when (detail) {
                is MovieDetailState.Error -> {
                    Text("Error")
                }

                MovieDetailState.Loading -> {
                    Text("Loading")
                }

                is MovieDetailState.Success -> {
                    MovieDetailsScreen(
                        modifier = Modifier.padding(padding).background(Color(0xFF0F1111)),
                        movie = (detail as MovieDetailState.Success).user,
                        navigator = navigator,
                        viewModel = viewModel
                    )
                }

                else -> {}
            }
        }
    }

    @Composable
    fun MovieDetailsScreen(
        movie: MovieDetailResponse,
        modifier: Modifier,
        navigator: Navigator,
        viewModel: DetailsScreenViewModel
    ) {

        LazyColumn(
            modifier = modifier.fillMaxSize().padding(top = 32.dp)
        )
        {
            header(movie = movie)
            detailsSection(
                movie = movie,
                navigator = navigator,
                viewModel = viewModel
            )

            item {
                Spacer(modifier = Modifier.height((75 + getNavigationBarHeight()).dp))
            }
        }

    }


    private fun LazyListScope.detailsSection(
        movie: MovieDetailResponse,
        navigator: Navigator,
        viewModel: DetailsScreenViewModel
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ShowDetailScreen(
                        movieId = movieId,
                        title = movie.title ?: "",
                        year = movie.publishAt ?: "",
                        ageRating = movie.publishAt ?: "",
                        genres = movie.genres ?: emptyList(),
                        description = movie.description ?: "",
                        viewModel = viewModel,
                        navigator = navigator,
                        onWatchClick = {
                            val data = movie.toMovie()
                            if (data.isSubscribed) navigator.push(PlayScreen(data))
                            else navigator.push(PricingPlanScreen(fromDetails = true))
                        }
                    )
                }

            }
        }
    }

    private fun LazyListScope.header(
        movie: MovieDetailResponse,
    ) {
        item {
            Box(
                Modifier.padding(horizontal = 16.dp).fillMaxWidth().height(400.dp)
                    .clip(RoundedCornerShape(4.dp))
            ) {
                CachedAsyncImage(
                    url = movie.thumbnail ?: "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ShowDetailScreen(
    movieId: Int,
    title: String,
    year: String,
    ageRating: String,
    genres: List<TagDTO>,
    description: String,
    viewModel: DetailsScreenViewModel,
    navigator: Navigator,
    onWatchClick: () -> Unit,
) {


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 28.sp,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = W500
            )
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(9.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(Color(0xFF83E8F4), shape = RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "HD",
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                )
            }

            if (year.isNotEmpty()) Text(
                text = year,
                fontSize = 14.sp,
                color = Color.White,
                modifier = Modifier.padding(3.dp)
            )

            genres.forEach { genre ->
                if (!genre.name.isNullOrEmpty()) {
                    Box(
                        modifier = Modifier
                            .background(Color(0x1AFFFFFF), shape = RoundedCornerShape(4.dp))
                    ) {
                        Text(
                            text = genre.name,
                            color = Color.White,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }

        Button(
            onClick = { onWatchClick() },
            modifier = Modifier.fillMaxWidth().height(52.dp).padding(bottom = 6.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF83E8F4),
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.play_circle),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = stringResource(Res.string.show),
                fontSize = 16.sp,
                color = Color(0xFF202123)
            )
        }


        Text(
            text = stringResource(Res.string.description),
            style = getTypography().titleLarge.copy(color = Color.White)
        )


        Text(
            text = description,
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily(
                    Font(
                        Res.font.inter_light,
                        FontWeight.Normal,
                        FontStyle.Normal
                    )
                ),
                fontWeight = FontWeight(700)
            ),
            color = Color(0x99FFFFFF),
            modifier = Modifier.fillMaxWidth(),
        )


        Text(
            text = stringResource(Res.string.leave_comment),
            style = getTypography().titleLarge.copy(color = Color.White)
        )


        StarRatingCommentBar(rating = viewModel.rating) { rating ->
            viewModel.onRatingChanged(rating)
        }


        CommentTextField(
            modifier = Modifier.fillMaxWidth().height(200.dp),
            comment = viewModel.comment,
            onCommentChanged = viewModel::onCommentChanged
        )

        MainButton(
            modifier = Modifier.fillMaxWidth().height(46.dp),
            text = stringResource(Res.string.send),
            isLoading = viewModel.post is PostCommentState.Loading,
            isEnable = viewModel.comment.isNotBlank() && viewModel.rating > 0f
        ) {
            viewModel.postComment()
        }


        when (val comments = viewModel.comments) {

            is CommentState.Comments -> {
                val length = if (comments.comments.size > 5) 5 else comments.comments.size

                if (length > 0) {
                    CommentListTopBar {
                        navigator.push(CommentsScreen(movieId = movieId))
                    }
                }

                Column {
                    repeat(length) { index ->
                        ReviewCard(comment = comments.comments[index])
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }

            is CommentState.Error -> {

            }

            CommentState.Idle -> {

            }

            CommentState.Loading -> {

            }
        }

    }
}

@Composable
fun CommentListTopBar(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(Res.string.comments),
            style = getTypography().titleLarge.copy(color = Color.White)
        )


        Text(
            text = stringResource(Res.string.see_all),
            modifier = Modifier.background(headlineMediumTextColorDark, RoundedCornerShape(36.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp).clickable(onClick = onClick),
            style = getTypography().titleMedium.copy(color = mainButtonTextColorDark)
        )

    }

}
