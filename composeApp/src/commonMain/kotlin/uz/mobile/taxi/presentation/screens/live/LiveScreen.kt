package uz.mobile.taxi.presentation.screens.live

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.cash.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import chaintech.network.connectivitymonitor.ConnectivityStatus
import chaintech.videoplayer.model.PlayerConfig
import chaintech.videoplayer.ui.video.VideoPlayerView
import taxi.composeapp.generated.resources.Res
import taxi.composeapp.generated.resources.ic_backward_10_video
import taxi.composeapp.generated.resources.ic_forward_10_video
import taxi.composeapp.generated.resources.ic_pause_video
import taxi.composeapp.generated.resources.ic_play_video
import taxi.composeapp.generated.resources.inter_light
import org.koin.compose.koinInject
import uz.mobile.taxi.data.remote.dto.Date
import uz.mobile.taxi.data.remote.dto.ProgramResponse
import uz.mobile.taxi.presentation.NetworkScreenModel
import uz.mobile.taxi.presentation.components.InternetOffline
import uz.mobile.taxi.presentation.components.MainTopBar
import uz.mobile.taxi.presentation.platform.MoviesAppScreen
import uz.mobile.taxi.presentation.sharedviewmodel.FullScreenStateModel
import uz.mobile.taxi.presentation.theme.backgroundDark
import uz.mobile.taxi.presentation.theme.getTypography
import uz.mobile.taxi.presentation.theme.headlineMediumTextColorDark
import uz.mobile.taxi.presentation.theme.secondaryButtonTextColorDark
import uz.mobile.taxi.presentation.theme.videoSeekbarInactiveColor

class LiveScreen : MoviesAppScreen {

    @Composable
    override fun Content() {
        val fullScreenStateModel:FullScreenStateModel = koinScreenModel()
        LiveMainScreen(
            fullScreenStateModel = fullScreenStateModel,
            showStatusBar = true
        )
    }
}

@Composable
fun LiveMainScreen(
    networkScreenModel: NetworkScreenModel = koinInject(),
    fullScreenStateModel: FullScreenStateModel,
    showStatusBar: Boolean,
) {

    val connectivityStatus by networkScreenModel.connectivityStatus.collectAsState()
    networkScreenModel.startMonitoring()
    when (connectivityStatus) {
        ConnectivityStatus.NOT_CONNECTED -> InternetOffline(tryAgain = { networkScreenModel.refresh() })
        else -> {
            ProgramScreenContent(
                fullScreenStateModel = fullScreenStateModel,
                showStatusBar = showStatusBar,
            )
        }
    }
}

@Composable
fun ProgramScreenContent(
    navigator: Navigator = LocalNavigator.currentOrThrow,
    screenViewModel: LiveScreenViewModel = koinInject(),
    fullScreenStateModel: FullScreenStateModel,
    showStatusBar: Boolean,
) {

    LaunchedEffect(Unit) {
        screenViewModel.getUrl()
        screenViewModel.getDate()
    }

    val live by screenViewModel.live.collectAsState()
    val dates by screenViewModel.dates.collectAsState()
    val programs = screenViewModel.program.collectAsLazyPagingItems()
    val isFullScreen: Boolean = fullScreenStateModel.isFullScreen


    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(if (!isFullScreen) WindowInsets.safeDrawing else WindowInsets(2.dp))
    ) {

        if (!isFullScreen && showStatusBar) {
            MainTopBar(onBackButtonClicked = { navigator.pop() }, text = "Live")
        }


        VideoPlayerBox(if (live is LiveState.Success) (live as LiveState.Success).movies.url else "", onFullScreenToggle = { isFS ->
            fullScreenStateModel.setFS(isFS)
        })


        Spacer(modifier = Modifier.height(20.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp)
        ) {
            val dates = if (dates is LiveDateState.Success) (dates as LiveDateState.Success).dates else null
            items(dates?.size ?: 0) { index ->
                val season = dates?.get(index)
                season?.let { date ->
                    ProgramTagItem(
                        season = date,
                        isSelected = season.id == screenViewModel.selectedDate?.id,
                        onSelected = {
                            screenViewModel.onTagSelected(date = date)
                            screenViewModel.selectedDate?.date?.let { date ->
                                screenViewModel.getProgram(date)
                            }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize().padding(horizontal = 16.dp)
                .background(Color(0xFF121212))
        ) {
            items(programs.itemCount) { program ->
                val pro = programs[program]
                pro?.let { ProgramItemView(programItem = it) }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

    }
}


@Composable
fun ProgramItemView(programItem: ProgramResponse.ScheduleProgram) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1C1C1E))
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        ) {

            Text(
                text = programItem.date ?: "",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(
                        org.jetbrains.compose.resources.Font(
                            Res.font.inter_light,
                            FontWeight.Normal,
                            FontStyle.Normal
                        )
                    ),
                    fontWeight = FontWeight(400),
                    color = Color(0xFFB9BFC1),
                )
            )

            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = programItem.title ?: "",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(
                        org.jetbrains.compose.resources.Font(
                            Res.font.inter_light,
                            FontWeight.Normal,
                            FontStyle.Normal
                        )
                    ),
                    fontWeight = FontWeight(700),
                    color = Color.White,
                )
            )
        }
    }
}


@Composable
private fun VideoPlayerBox(
    url: String?,
    onFullScreenToggle: (Boolean) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopStart
    ) {
        VideoPlayerView(
            modifier = Modifier.height(224.dp).fillMaxWidth().background(backgroundDark),
            url = url ?: "",
            playerConfig = PlayerConfig(
                pauseIconResource = Res.drawable.ic_pause_video,
                playIconResource = Res.drawable.ic_play_video,
                fastForwardIconResource = Res.drawable.ic_forward_10_video,
                fastBackwardIconResource = Res.drawable.ic_backward_10_video,
                topControlSize = 20.dp,
                controlTopPadding = 12.dp,
                seekBarThumbColor = secondaryButtonTextColorDark,
                seekBarActiveTrackColor = secondaryButtonTextColorDark,
                seekBarInactiveTrackColor = videoSeekbarInactiveColor,
                pauseResumeIconSize = 46.dp,
                fastForwardBackwardIconSize = 40.dp,
                durationTextStyle = getTypography().titleLarge,
            ),
            fullScreenToggle = onFullScreenToggle,
            onCurrentTime = {

            }
        )
    }
}


@Composable
fun ProgramTagItem(
    season: Date,
    isSelected: Boolean = false,
    onSelected: (Date) -> Unit,
    modifier: Modifier = Modifier.padding(vertical = 16.dp),
) {

    val bgColor = if (isSelected) headlineMediumTextColorDark else Color.Transparent
    val textStyle = if (isSelected) getTypography().labelMedium.copy(lineHeight = 16.sp)
    else getTypography().bodySmall.copy(lineHeight = 16.sp, color = headlineMediumTextColorDark)

    androidx.compose.material.Text(
        text = season.date.orEmpty().ifEmpty { "Season" },
        modifier = modifier.background(
            color = bgColor,
            shape = RoundedCornerShape(50)
        ).padding(horizontal = 20.dp, vertical = 8.dp).clickable { onSelected(season) },
        style = textStyle
    )
}



