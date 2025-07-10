package uz.mobile.taxi.presentation.screens.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import chaintech.network.connectivitymonitor.ConnectivityStatus
import taxi.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import uz.mobile.taxi.domain.model.Tag
import uz.mobile.taxi.presentation.NetworkScreenModel
import uz.mobile.taxi.presentation.components.InputTextField
import uz.mobile.taxi.presentation.components.InternetOffline
import uz.mobile.taxi.presentation.components.MovieCardSmall
import uz.mobile.taxi.presentation.platform.MoviesAppScreen
import uz.mobile.taxi.presentation.screens.naviagationActions.navigateToMovieDetailScreen
import uz.mobile.taxi.presentation.theme.getTypography
import uz.mobile.taxi.presentation.theme.headlineMediumTextColorDark
import uz.mobile.taxi.presentation.theme.hintTextColorDark
import uz.mobile.taxi.presentation.util.getNavigationBarHeight


class SearchScreenContent: MoviesAppScreen {

    @Composable
    override fun Content() {
        val networkScreenModel = remember { NetworkScreenModel() }
        val connectivityStatus by networkScreenModel.connectivityStatus.collectAsState()
        networkScreenModel.startMonitoring()
        when (connectivityStatus) {
            ConnectivityStatus.NOT_CONNECTED -> InternetOffline(tryAgain = { networkScreenModel.refresh() })
            else -> {
                SearchChecked()
            }
        }
    }

}

@Composable
fun SearchChecked(
    navigator: Navigator = LocalNavigator.currentOrThrow,
    viewModel: SearchScreenViewModel = koinInject()
) {
    val tags = viewModel.tags
    val selectedTagId = viewModel.selectedTagId
    val state = viewModel.state

    LaunchedEffect(tags) {
        viewModel.getTags()
    }


    Column(
        modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)
    ) {

        Spacer(modifier = Modifier.height(24.dp))

        InputTextField(
            modifier = Modifier.padding(horizontal = 20.dp),
            value = viewModel.searchText,
            onValueChange = viewModel::onSearchTextChange,
            leadingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.ic_search),
                    contentDescription = "search",
                    tint = headlineMediumTextColorDark
                )
            }, placeholder = {
                Text(
                    text = stringResource(Res.string.search),
                    style = getTypography().bodyMedium.copy(color = hintTextColorDark)
                )
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp)
        ) {
            items(tags.size) { index ->
                val tag = tags[index]
                TagItem(
                    tag = tag,
                    isSelected = tag.id == selectedTagId,
                    onSelected = { data -> viewModel.onTagSelected(id = data.id) }
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        when (state) {
            is MoviesState.Success -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 32.dp)
                ) {
                    items(state.movies.size) { index ->
                        val movie = state.movies[index]
                        MovieCardSmall(
                            movie = movie,
                            onClick = {
                                navigateToMovieDetailScreen(
                                    navigator = navigator,
                                    movie.id, movie.title
                                )
                            }
                        )
                    }

                    item {
                       Spacer(modifier = Modifier.height((75 + getNavigationBarHeight()).dp))
                    }
                }
            }

            MoviesState.Empty -> {
                SearchPlaceholder(
                    modifier = Modifier.fillMaxSize()
                )
            }

            MoviesState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            MoviesState.Idl -> {
                SearchPlaceholder(
                    modifier = Modifier.fillMaxWidth().weight(1f)
                )
            }

            is MoviesState.Error -> {

            }
        }


    }
}


@Composable
fun SearchPlaceholder(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            modifier = Modifier.size(140.dp),
            painter = painterResource(Res.drawable.ic_search_placeholder),
            contentDescription = "empty"
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(Res.string.nothing_found),
            style = getTypography().bodyMedium.copy(color = headlineMediumTextColorDark)
        )
    }
}


@Composable
fun TagItem(
    tag: Tag,
    isSelected: Boolean = false,
    onSelected: (Tag) -> Unit,
    modifier: Modifier = Modifier
) {

    val bgColor = if (isSelected) headlineMediumTextColorDark else Color.Transparent
    val textStyle = if (isSelected) getTypography().labelMedium.copy(lineHeight = 16.sp)
    else getTypography().bodySmall.copy(lineHeight = 16.sp, color = headlineMediumTextColorDark)


    Text(
        text = tag.name.orEmpty(),
        modifier = modifier.background(
            color = bgColor,
            shape = RoundedCornerShape(50)
        ).padding(horizontal = 20.dp, vertical = 8.dp).clickable { onSelected(tag) },
        style = textStyle
    )


}



