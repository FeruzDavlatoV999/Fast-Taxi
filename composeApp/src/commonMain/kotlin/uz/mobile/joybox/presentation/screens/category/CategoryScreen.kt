package uz.mobile.joybox.presentation.screens.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import app.cash.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import chaintech.network.connectivitymonitor.ConnectivityStatus
import org.koin.compose.koinInject
import uz.mobile.joybox.presentation.NetworkScreenModel
import uz.mobile.joybox.presentation.components.InternetOffline
import uz.mobile.joybox.presentation.components.MainTopBar
import uz.mobile.joybox.presentation.components.MovieCardSmall
import uz.mobile.joybox.presentation.platform.MoviesAppScreen
import uz.mobile.joybox.presentation.screens.naviagationActions.navigateToMovieDetailScreen


class CategoryScreen(
    private val id: Int,
    private val name: String
) : MoviesAppScreen {

    @Composable
    override fun Content() {
        val networkScreenModel = remember { NetworkScreenModel() }
        val connectivityStatus by networkScreenModel.connectivityStatus.collectAsState()
        networkScreenModel.startMonitoring()
        when (connectivityStatus) {
            ConnectivityStatus.NOT_CONNECTED -> InternetOffline(tryAgain = { networkScreenModel.refresh() })
            else -> {
                Category(movieId = id, name = name)
            }
        }
    }

}

@Composable
fun Category(
    screenModel: CategoryViewModel = koinInject(),
    movieId: Int,
    name: String,
    navigator: Navigator = LocalNavigator.currentOrThrow
) {
    val courses = screenModel.categoriesWithCourses.collectAsLazyPagingItems()


    LaunchedEffect(movieId) {
        screenModel.fetchCategoriesWithCourses(movieId)
    }

    Column(
        modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        MainTopBar(onBackButtonClicked = {
            navigator.pop()
        }, text = name)

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(16.dp)
        ) {

            items(courses.itemCount) { index ->
                val course = courses[index]
                course?.let {
                    MovieCardSmall(movie = it, onClick = {
                        navigateToMovieDetailScreen(
                            navigator = navigator,
                            it.id ?: 0,
                            it.title ?: ""
                        )
                    })
                }
            }

            item {
                Spacer(Modifier.height(70.dp))
            }

            courses.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        item(span = { GridItemSpan(2) }) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = Color.Cyan)
                            }
                        }
                    }

                    loadState.append is LoadState.Error -> {

                        item(span = { GridItemSpan(2) }) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Error loading more courses", color = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}


