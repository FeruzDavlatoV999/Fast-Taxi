package uz.mobile.joybox.presentation.screens.play

import ContentWithMessageBar
import MessageBarPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import chaintech.network.connectivitymonitor.ConnectivityStatus
import chaintech.videoplayer.model.PlayerConfig
import chaintech.videoplayer.ui.video.VideoPlayerView
import joybox.composeapp.generated.resources.Res
import joybox.composeapp.generated.resources.ic_backward_10_video
import joybox.composeapp.generated.resources.ic_forward_10_video
import joybox.composeapp.generated.resources.ic_pause_video
import joybox.composeapp.generated.resources.ic_play_video
import org.koin.core.parameter.parametersOf
import rememberMessageBarState
import uz.mobile.joybox.domain.model.Movie
import uz.mobile.joybox.presentation.NetworkScreenModel
import uz.mobile.joybox.presentation.components.EpisodeItem
import uz.mobile.joybox.presentation.components.MainTopBar
import uz.mobile.joybox.presentation.components.SeasonTagItem
import uz.mobile.joybox.presentation.platform.MoviesAppScreen
import uz.mobile.joybox.presentation.sharedviewmodel.FullScreenStateModel
import uz.mobile.joybox.presentation.theme.getTypography
import uz.mobile.joybox.presentation.theme.secondaryButtonTextColorDark
import uz.mobile.joybox.presentation.theme.videoSeekbarInactiveColor
import uz.mobile.joybox.presentation.util.setFullScreen
import uz.mobile.joybox.presentation.util.getNavigationBarHeight

class PlayScreen(private val movie: Movie) : MoviesAppScreen {

    @Composable
    override fun Content() {
        val networkScreenModel = remember { NetworkScreenModel() }
        var checkNetwork by remember { mutableStateOf(false) }
        val connectivityStatus by networkScreenModel.connectivityStatus.collectAsState()
        val fullScreenStateModel:FullScreenStateModel = koinScreenModel()

        checkNetwork = connectivityStatus != ConnectivityStatus.NOT_CONNECTED

        PlayScreenContent(
            checkInternet = checkNetwork,
            fullScreenStateModel = fullScreenStateModel
        )
    }


    @Composable
    fun PlayScreenContent(
        navigator: Navigator = LocalNavigator.currentOrThrow,
        viewModel: PlayScreenViewModel = koinScreenModel { parametersOf(movie) },
        fullScreenStateModel: FullScreenStateModel,
        checkInternet: Boolean,
    ) {
        val seasons = viewModel.seasons
        val episodes = viewModel.episodes
        val selectedSeason = viewModel.selectedSeason
        val selectedEpisode: Movie = viewModel.selectedEpisode
        val isFullScreen: Boolean = fullScreenStateModel.isFullScreen

        val messageBarState = rememberMessageBarState()


        LaunchedEffect(isFullScreen){
            setFullScreen(isFullScreen)
        }



        if (!checkInternet) {
            ContentWithMessageBar(
                position = MessageBarPosition.TOP,
                messageBarState = messageBarState,
                showCopyButton = false,
                verticalPadding = 32.dp,
            ) {
                messageBarState.addError(Exception("Что-то не так с интернетом!"))
            }
        }

        Column(
            modifier = Modifier
                .windowInsetsPadding(if (!isFullScreen) WindowInsets.safeDrawing else WindowInsets(2.dp))
                .fillMaxSize()
        ) {

            if (!isFullScreen) {
                MainTopBar(onBackButtonClicked = { navigator.pop() }, text = movie.title)
            }

            VideoPlayerBox(
                modifier = Modifier.height(224.dp).fillMaxWidth().windowInsetsPadding(WindowInsets.safeDrawing),
                movie = selectedEpisode,
                isFullScreen = isFullScreen,
                onCurrentTime = { time ->
                    viewModel.updateWatchableInfo(selectedEpisode.id, selectedEpisode.type, time)
                }
            ) { isFS ->
                fullScreenStateModel.setFS(isFS)
            }
            Spacer(modifier = Modifier.height(20.dp))

            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp)
            ) {
                items(seasons.size) { index ->
                    val season = seasons[index]
                    SeasonTagItem(
                        season = season,
                        isSelected = season.id == selectedSeason?.id,
                        onSelected = { data -> viewModel.selectSeason(data = data) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))


            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 4.dp,
                    bottom = 32.dp
                )
            ) {
                items(episodes.size) { index ->
                    val chapter = episodes[index]
                    EpisodeItem(
                        movie = chapter,
                        isSelected = selectedEpisode.id == chapter.id
                    ) { chptr ->
                        viewModel.selectEpisode(chptr)
                    }
                }
                item {
                    Spacer(modifier = Modifier.height((75 + getNavigationBarHeight()).dp))
                }
            }

        }

    }

    @Composable
    private fun VideoPlayerBox(
        modifier: Modifier = Modifier,
        movie: Movie,
        onCurrentTime: ((Int) -> Unit),
        isFullScreen:Boolean = false,
        onFullScreenToggle: (Boolean) -> Unit
    ) {
        VideoPlayerView(
            modifier = modifier,
            url = movie.url,
            playerConfig = PlayerConfig(
                pauseIconResource = Res.drawable.ic_pause_video,
                playIconResource = Res.drawable.ic_play_video,
                fastForwardIconResource = Res.drawable.ic_forward_10_video,
                fastBackwardIconResource = Res.drawable.ic_backward_10_video,
                topControlSize = 22.dp,
                controlTopPadding = if (isFullScreen) 12.dp else 0.dp,
                seekBarThumbColor = secondaryButtonTextColorDark,
                seekBarActiveTrackColor = secondaryButtonTextColorDark,
                seekBarInactiveTrackColor = videoSeekbarInactiveColor,
                pauseResumeIconSize = 46.dp,
                fastForwardBackwardIconSize = 40.dp,
                durationTextStyle = getTypography().titleLarge
            ),
            fullScreenToggle = onFullScreenToggle,
            onCurrentTime = {
                onCurrentTime(it)
            }
        )
    }

}




