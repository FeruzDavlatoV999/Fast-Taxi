package chaintech.videoplayer.ui.youtube

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import chaintech.videoplayer.host.MediaPlayerHost
import chaintech.videoplayer.model.VideoPlayerConfig
import chaintech.videoplayer.model.PlayerSpeed
import chaintech.videoplayer.ui.video.controls.BottomControls
import chaintech.videoplayer.ui.video.controls.CenterControls
import chaintech.videoplayer.ui.video.controls.LockScreenComponent
import chaintech.videoplayer.ui.video.controls.SpeedSelectionOverlay
import chaintech.videoplayer.ui.video.controls.TopControls
import chaintech.videoplayer.util.extractYouTubeVideoId
import chaintech.videoplayer.util.rememberAppBackgroundObserver
import chaintech.videoplayer.util.youtubeProgressColor
import chaintech.videoplayer.youtube.YoutubeEvent
import chaintech.videoplayer.youtube.YoutubeHost
import chaintech.videoplayer.youtube.YoutubePlayer
import chaintech.videoplayer.youtube.YoutubePlayerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


@Composable
internal fun YoutubePlayerWithControl(
    modifier: Modifier,
    playerHost: MediaPlayerHost,
    playerConfig: VideoPlayerConfig, // Configuration for the player
    showControls: Boolean, // Flag indicating if controls should be shown
    onShowControlsToggle: (() -> Unit) // Callback for toggling show/hide controls
) {
    val coroutineScope = rememberCoroutineScope()
    val hostState = remember { YoutubeHost() }

    var showSpeedSelection by remember { mutableStateOf(false) } // Selected playback speed
    var isScreenLocked by remember { mutableStateOf(false) }
    var isInitializing by remember { mutableStateOf(true) }
    var pause by remember { mutableStateOf(playerHost.isPaused) }
    var isVideoStarted by remember { mutableStateOf(false) }
    var isCooldownActive by remember { mutableStateOf(false) }

    pause = playerHost.isPaused
    playerConfig.isScreenResizeEnabled = false

    // Observe app background state
    val appBackgroundObserver = rememberAppBackgroundObserver()
    LaunchedEffect(Unit) {
        appBackgroundObserver.observe {
            if (!pause) {
                playerHost.togglePlayPause()
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose { appBackgroundObserver.removeObserver() }
    }

    // React to URL changes
    LaunchedEffect(playerHost.url) {
        isVideoStarted = false
        val videoId = extractYouTubeVideoId(playerHost.url) ?: playerHost.url
        coroutineScope.launch { hostState.loadVideo(videoId) }
    }
// Update player callbacks
    LaunchedEffect(hostState.isBuffer) {
        playerHost.setBufferingStatus(hostState.isBuffer)
    }

    LaunchedEffect(playerHost.seekToTime) {
        playerHost.seekToTime?.let {
            coroutineScope.launch {
                hostState.seekTo(it.seconds)
                playerHost.seekToTime = null
            }
        }
    }
    fun handleEndVideo(status: YoutubeEvent.StatusUpdated.PlaybackStatus) {
        if (status == YoutubeEvent.StatusUpdated.PlaybackStatus.FINISHED) {
            if (isCooldownActive) return

            isCooldownActive = true
            playerHost.triggerMediaEnd()

            coroutineScope.launch {
                if (playerHost.isLooping) {
                    hostState.seekTo(0.seconds)
                    hostState.play()
                } else {
                    hostState.seekTo(0.seconds)
                    if (!playerHost.isPaused) {
                        playerHost.togglePlayPause()
                    }
                }

                delay(1000)
                isCooldownActive = false
            }
        }
    }

    fun handleStartTime() {
        playerHost.playFromTime?.let {
            coroutineScope.launch {
                hostState.seekTo(it.seconds)
            }
            playerHost.playFromTime = null
        }
    }

    fun setSpeed() {
        coroutineScope.launch {
            if (hostState.state is YoutubePlayerState.PlayingState) {
                hostState.setPlaybackRate(
                    when (playerHost.speed) {
                        PlayerSpeed.X0_5 -> 0.5f
                        PlayerSpeed.X1 -> 1f
                        PlayerSpeed.X1_5 -> 1.5f
                        PlayerSpeed.X2 -> 2f
                    }
                )
            }
        }
    }

    when (val state = hostState.state) {
        is YoutubePlayerState.ErrorState -> isInitializing = false

        YoutubePlayerState.Idle -> isInitializing = true

        is YoutubePlayerState.PlayingState -> {
            playerHost.updateTotalTime(state.totalDuration.inWholeSeconds.toInt())
            if (!playerHost.isSliding) {
                playerHost.updateCurrentTime(state.currentTime.inWholeSeconds.toInt())
            }
            handleEndVideo(state.playbackStatus)
            handleStartTime()
            if (!isVideoStarted) {
                isVideoStarted = true
                coroutineScope.launch {
                    if (playerHost.isMuted) { hostState.mute() } else { hostState.unmute() }
                    if (playerHost.isPaused) { hostState.pause()  } else { hostState.play() }
                }
                setSpeed()
            }
            isInitializing = false
        }

        YoutubePlayerState.Initialized -> coroutineScope.launch {
            val videoId = extractYouTubeVideoId(playerHost.url) ?: playerHost.url
            hostState.loadVideo(videoId)
        }
    }

    // React to state changes
    LaunchedEffect(playerHost.isPaused) {
        coroutineScope.launch {
            if (hostState.state is YoutubePlayerState.PlayingState) {
                if (playerHost.isPaused) {
                    hostState.pause()
                } else {
                    hostState.play()
                }
            }
        }
    }
    LaunchedEffect(playerHost.isMuted) {
        coroutineScope.launch {
            if (hostState.state is YoutubePlayerState.PlayingState) {
                if (playerHost.isMuted) {
                    hostState.mute()
                } else {
                    hostState.unmute()
                }
            }
        }
    }
    LaunchedEffect(playerHost.speed) {
        setSpeed()
    }

    val scale = remember { mutableStateOf(1f) }
    val transformableState = rememberTransformableState { zoomChange, _, _ ->
        scale.value = (scale.value * zoomChange).coerceIn(0.5f, 3f) // Limit zoom between 1x and 3x
    }

    // Container for the video player and control components
    Box(
        modifier = modifier
            .clipToBounds() // Ensures the zoomed content stays within the box
            .then(
                if (!isScreenLocked && playerHost.isZoomEnabled) Modifier.transformable(transformableState)
                else Modifier // No zoom when screen is locked
            )
    ) {
        // Video player component
        YoutubePlayer(
            modifier = modifier
                .graphicsLayer(
                    scaleX = scale.value,
                    scaleY = scale.value,
                    transformOrigin = TransformOrigin.Center
                ),
            host = hostState,
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (isInitializing) {
                        Color.Black
                    } else {
                        Color.Transparent
                    }
                )
                .pointerInput(Unit) {
                    detectTapGestures { _ ->
                        onShowControlsToggle() // Toggle show/hide controls on tap
                        showSpeedSelection = false
                    }
                }
                .wrapContentSize(align = Alignment.Center)
        ) {
            if (isInitializing) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(80.dp)
                        .scale(1.5f)
                        .wrapContentSize(align = Alignment.Center), // Center the progress indicator
                    color = youtubeProgressColor
                )
            }
        }

        if (isScreenLocked.not()) {
            // Top control view for playback speed and mute/unMute
            TopControls(
                playerConfig = playerConfig,
                isMute = playerHost.isMuted,
                onMuteToggle = {
                    playerHost.toggleMuteUnmute()
                }, // Toggle mute/unMute
                showControls = showControls, // Pass show/hide controls state
                onTapSpeed = { showSpeedSelection = showSpeedSelection.not() },
                isFullScreen = playerHost.isFullScreen,
                onFullScreenToggle = { playerHost.toggleFullScreen() },
                onLockScreenToggle = { isScreenLocked = isScreenLocked.not() },
                onResizeScreenToggle = { }
            )

            // Center control view for pause/resume and fast forward/backward actions
            CenterControls(
                playerConfig = playerConfig,
                isPause = playerHost.isPaused,
                onPauseToggle = {
                    playerHost.togglePlayPause()
                },
                onBackwardToggle = { // Seek backward
                    coroutineScope.launch {
                        hostState.seekTo(
                            maxOf(
                                0,
                                playerHost.currentTime - playerConfig.fastForwardBackwardIntervalSeconds
                            ).seconds
                        )
                    }
                },
                onForwardToggle = { // Seek forward
                    coroutineScope.launch {
                        hostState.seekTo(
                            minOf(
                                playerHost.totalTime,
                                playerHost.currentTime + playerConfig.fastForwardBackwardIntervalSeconds
                            ).seconds
                        )
                    }
                },
                showControls = showControls
            )

            // Bottom control view for seek bar and time duration display
            BottomControls(
                playerConfig = playerConfig,
                currentTime = playerHost.currentTime, // Pass current playback time
                totalTime = playerHost.totalTime, // Pass total duration of the video
                showControls = showControls, // Pass show/hide controls state
                onChangeSliderTime = {
                    it?.let {
                        coroutineScope.launch {
                            hostState.seekTo(it.seconds)
                        }
                    }
                }, // Update seek bar slider time
                onChangeCurrentTime = { playerHost.updateCurrentTime(it) }, // Update current playback time
                onChangeSliding = { // Update seek bar sliding state
                    playerHost.isSliding = it
                }
            )

        } else {
            if (playerConfig.isScreenLockEnabled) {
                LockScreenComponent(
                    playerConfig = playerConfig,
                    showControls = showControls,
                    onLockScreenToggle = { isScreenLocked = isScreenLocked.not() }
                )
            }
        }
        SpeedSelectionOverlay(
            playerConfig = playerConfig,
            selectedSpeed = playerHost.speed,
            selectedSpeedCallback = { playerHost.setSpeed(it) },
            showSpeedSelection = showSpeedSelection,
            showSpeedSelectionCallback = { showSpeedSelection = it }
        )
    }
}