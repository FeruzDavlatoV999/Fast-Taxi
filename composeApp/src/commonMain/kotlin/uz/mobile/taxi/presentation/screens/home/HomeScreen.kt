package uz.mobile.taxi.presentation.screens.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import chaintech.network.connectivitymonitor.ConnectivityStatus
import kotlinx.coroutines.delay
import taxi.composeapp.generated.resources.Res
import taxi.composeapp.generated.resources.all
import taxi.composeapp.generated.resources.catolog
import taxi.composeapp.generated.resources.ic_notification
import taxi.composeapp.generated.resources.ic_play_small
import taxi.composeapp.generated.resources.inter_medium
import taxi.composeapp.generated.resources.looked_movie
import taxi.composeapp.generated.resources.makon_tv_live
import taxi.composeapp.generated.resources.onboarding_logo
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import uz.mobile.taxi.data.remote.dto.BannersResponse
import uz.mobile.taxi.data.remote.dto.CategoryResponse
import uz.mobile.taxi.data.remote.dto.TagDTO
import uz.mobile.taxi.domain.model.Movie
import uz.mobile.taxi.presentation.NetworkScreenModel
import uz.mobile.taxi.presentation.components.CachedAsyncImage
import uz.mobile.taxi.presentation.components.InternetOffline
import uz.mobile.taxi.presentation.components.MainButton
import uz.mobile.taxi.presentation.components.MovieCardSmall
import uz.mobile.taxi.presentation.components.PageIndicator
import uz.mobile.taxi.presentation.components.PagerMovieCard
import uz.mobile.taxi.presentation.platform.MoviesAppScreen
import uz.mobile.taxi.presentation.screens.naviagationActions.navigateToCategoryScreen
import uz.mobile.taxi.presentation.screens.naviagationActions.navigateToLiveScreen
import uz.mobile.taxi.presentation.screens.naviagationActions.navigateToMovieDetailScreen
import uz.mobile.taxi.presentation.screens.notifications.NotificationsScreen
import uz.mobile.taxi.presentation.screens.play.PlayScreen
import uz.mobile.taxi.presentation.util.getNavigationBarHeight

class HomeScreen : MoviesAppScreen {

    @Composable
    override fun Content() {
        val networkScreenModel: NetworkScreenModel = koinInject()
        val connectivityStatus by networkScreenModel.connectivityStatus.collectAsState()
        when (connectivityStatus) {
            ConnectivityStatus.NOT_CONNECTED -> {
                InternetOffline(tryAgain = { networkScreenModel.refresh() })
            }

            else -> {
                HomeCheckedScreen()
            }
        }
    }
}

@Composable
fun HomeCheckedScreen(
    screenModel: HomeScreenViewModel = koinInject(),
    navigator: Navigator = LocalNavigator.currentOrThrow,
) {
    val currentPagerSize by rememberSaveable { mutableStateOf(380) }
    val categories = screenModel.categoriesWithCourses.collectAsLazyPagingItems()
    val historyMovie = screenModel.historyMovie.collectAsLazyPagingItems()
    val getTags by screenModel.getTags.collectAsState()
    val banners = screenModel.getBanners.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        screenModel.launch()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F1111))
            .consumeWindowInsets(WindowInsets.safeDrawing),
        contentAlignment = Alignment.TopStart
    ) {
        LazyColumn {

            item {

                Box {
                    AutoScrollingHorizontalSlider(
                        banners = banners,
                        height = currentPagerSize
                    ) { page, banner ->
                        if (banner != null) {
                            PagerMovieCard(movie = banner)
                        } else {
                            Box(
                                modifier = Modifier.fillMaxSize().background(Color.Gray)
                            )
                        }
                    }


                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .padding(top = 48.dp, start = 20.dp, end = 20.dp).align(Alignment.TopEnd),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Image(
                            painter = painterResource(Res.drawable.onboarding_logo),
                            contentDescription = "logo",
                            modifier = Modifier.size(height = 44.dp, width = 36.dp)
                        )

                        Image(
                            painter = painterResource(Res.drawable.ic_notification),
                            contentDescription = "notifications",
                            modifier = Modifier.clickable {
                                navigator.push(NotificationsScreen())
                            }
                        )
                    }
                }

                MainButton(
                    text = stringResource(Res.string.makon_tv_live),
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 21.dp)
                        .height(46.dp)
                ) {
                    navigateToLiveScreen(navigator = navigator)
                }

                TagSection(getTags, screenModel)

                ViewedSection(
                    videos = historyMovie,
                    onPlayClick = { movie ->
                        navigator.push(PlayScreen(movie))
                    }
                )

                for (i in 0 until categories.itemCount) {
                    key(screenModel.selectedTab) {
                        categories[i]?.let { category ->
                            CategoryItem(category = category, navigator = navigator)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                categories.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> LoadingIndicator()
                        loadState.append is LoadState.Error -> ErrorIndicator()
                    }
                }

                Spacer(modifier = Modifier.height((75 + getNavigationBarHeight()).dp))
            }
        }
    }
}

@Composable
fun TagSection(
    getTags: TagsState,
    screenModel: HomeScreenViewModel,
) {
    when (getTags) {
        is TagsState.SuccessTags -> {
            (getTags).tags?.tags?.let {
                val allTag = TagDTO(name = stringResource(Res.string.all))
                val tagsList = listOf(allTag) + it
                HomeScreenTabs(
                    tabItems = tagsList,
                    selectedTabIndex = screenModel.selectedTab,
                    onTabSelected = { index ->
                        screenModel.updateTab(index)
                    },
                    onTabSlug = { slug ->
                        if (slug.isNotEmpty()) {
                            screenModel.fetchCategoriesWithCourses(true, slug, false)
                        } else {
                            screenModel.fetchCategoriesWithCourses(true, showOnHomepage = true)
                        }
                    }
                )
            }
        }

        is TagsState.Loading -> {}
        is TagsState.ErrorTags -> {}
    }
}


@Composable
fun ViewedSection(
    videos: LazyPagingItems<Movie>,
    onPlayClick: (Movie) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (videos.itemCount > 0) {
            Text(
                text = stringResource(Res.string.looked_movie),
                style = TextStyle(
                    fontSize = 18.sp,
                    lineHeight = 25.2.sp,
                    fontFamily = FontFamily(
                        Font(
                            Res.font.inter_medium,
                            FontWeight.Normal,
                            FontStyle.Normal
                        )
                    ),
                    fontWeight = FontWeight(600)
                ),
                modifier = Modifier
                    .padding(16.dp, 8.dp, 16.dp, 16.dp),
                color = Color.White
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .nestedScroll(remember { object : NestedScrollConnection {} })
        ) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(videos.itemCount) { video ->
                    val film = videos[video]
                    film?.let {
                        HistoryMovieItem(movie = film, onPlayClick = onPlayClick)
                    }
                }
            }
        }
    }
}


@Composable
fun HistoryMovieItem(
    movie: Movie,
    onPlayClick: (Movie) -> Unit,
) {
    Box(
        modifier = Modifier.width(220.dp).padding(end = 6.dp)
            .clip(RoundedCornerShape(10.dp)).fillMaxWidth()
            .clickable { onPlayClick(movie) }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                CachedAsyncImage(
                    url = movie.image,
                    modifier = Modifier.fillMaxWidth().height(104.dp)
                        .clip(RoundedCornerShape(10.dp)),
                )
                Icon(
                    painter = painterResource(Res.drawable.ic_play_small),
                    contentDescription = "Play",
                    modifier = Modifier.align(Alignment.Center).size(20.dp),
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun CategoryItem(
    category: CategoryResponse.Category,
    navigator: Navigator,
) {

    Column(modifier = Modifier.graphicsLayer {
        alpha = 0.99f
    }) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp, top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = category.name ?: "",
                style = TextStyle(
                    fontSize = 18.sp,
                    lineHeight = 25.2.sp,
                    fontFamily = FontFamily(
                        Font(
                            Res.font.inter_medium,
                            FontWeight.Normal,
                            FontStyle.Normal
                        )
                    ),
                    fontWeight = FontWeight(600)
                ),
                modifier = Modifier.padding(16.dp),
                color = Color.White
            )

            Box(
                modifier = Modifier.padding(end = 16.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(50),
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        navigateToCategoryScreen(
                            navigator = navigator,
                            category.id ?: 0,
                            category.name ?: ""
                        )
                    }
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = stringResource(Res.string.catolog),
                    fontSize = 14.sp,
                    color = Color.Black,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(
                            Font(
                                Res.font.inter_medium,
                                FontWeight.Normal,
                                FontStyle.Normal
                            )
                        ),
                        fontWeight = FontWeight(600)
                    )
                )
            }
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(category.courses ?: emptyList()) { course ->
                MovieCardSmall(movie = course, onClick = {
                    navigateToMovieDetailScreen(
                        navigator = navigator,
                        course.id ?: 0,
                        course.title ?: ""
                    )
                })
            }
        }
    }
}


@Composable
fun AutoScrollingHorizontalSlider(
    banners: LazyPagingItems<BannersResponse.Banner>,
    delay: Long = 5000,
    animationDuration: Int = 2000,
    height: Int = 380,
    content: @Composable (page: Int, banner: BannersResponse.Banner?) -> Unit,
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { banners.itemCount }
    )

    LaunchedEffect(banners.itemCount) {
        if (banners.itemCount > 0) {
            while (true) {
                if (!pagerState.isScrollInProgress) {
                    val nextPage = (pagerState.currentPage + 1) % banners.itemCount
                    pagerState.animateScrollToPage(
                        page = nextPage,
                        animationSpec = tween(
                            durationMillis = animationDuration,
                            easing = FastOutSlowInEasing
                        )
                    )
                }
                delay(delay)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.align(Alignment.Center),
            pageSpacing = 0.dp
        ) { page ->
            val banner = banners[page]
            content(page, banner)
        }
        PageIndicator(
            pageCount = banners.itemCount,
            currentPage = pagerState.currentPage,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}


@Composable
fun HomeScreenTabs(
    tabItems: List<TagDTO>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    onTabSlug: (String) -> Unit,
) {
    Box(
        modifier = Modifier.padding(bottom = 16.dp)
            .fillMaxWidth()
            .background(Color(0xFF0F1111))
    ) {
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            edgePadding = 16.dp,
            containerColor = Color.Transparent,
            indicator = { },
            divider = { }
        ) {
            tabItems.forEachIndexed { index, title ->
                val isSelected = selectedTabIndex == index
                val backgroundColor = if (isSelected) Color.White else Color.Transparent
                val textColor = if (isSelected) Color.Black else Color.White

                Box(
                    modifier = Modifier
                        .background(
                            color = backgroundColor,
                            shape = RoundedCornerShape(50)
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onTabSelected(index)
                            onTabSlug(title.slug ?: "")
                        }.padding(horizontal = 16.dp)
                ) {
                    Text(
                        fontSize = 14.sp,
                        text = title.name ?: "",
                        color = textColor,
                        modifier = Modifier.align(Alignment.Center).padding(vertical = 8.dp),
                        style = TextStyle(
                            fontFamily = FontFamily(
                                Font(
                                    Res.font.inter_medium,
                                    FontWeight.Normal,
                                    FontStyle.Normal
                                )
                            ),
                            fontWeight = FontWeight(500)
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.Cyan)
    }
}

@Composable
fun ErrorIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Error loading more courses", color = Color.Red)
    }
}









