package chaintech.videoplayer.ui.video

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import chaintech.videoplayer.host.MediaPlayerHost
import chaintech.videoplayer.model.ScreenResize
import chaintech.videoplayer.model.VideoPlayerConfig
import chaintech.videoplayer.ui.component.LiveStreamComposable
import chaintech.videoplayer.ui.video.controls.BottomControls
import chaintech.videoplayer.ui.video.controls.CenterControls
import chaintech.videoplayer.ui.video.controls.LockScreenComponent
import chaintech.videoplayer.ui.video.controls.SpeedSelectionOverlay
import chaintech.videoplayer.ui.video.controls.TopControls
import chaintech.videoplayer.util.CMPPlayer

@Composable
internal fun VideoPlayerWithControl(
    modifier: Modifier,
    playerHost: MediaPlayerHost,
    playerConfig: VideoPlayerConfig, // Configuration for the player
    showControls: Boolean, // Flag indicating if controls should be shown
    onShowControlsToggle: (() -> Unit), // Callback for toggling show/hide controls
) {
    var showSpeedSelection by remember { mutableStateOf(false) } // Selected playback speed
    var isScreenLocked by remember { mutableStateOf(false) }

    LaunchedEffect(playerHost.totalTime) {
        if (playerHost.totalTime > 0 && !playerConfig.isLiveStream) {
            playerHost.playFromTime?.let {
                if (!playerConfig.isLiveStream) {
                    playerHost.isSliding = true
                    playerHost.seekToTime = it
                    playerHost.isSliding = false
                }
                playerHost.playFromTime = null
            }
        }
    }

    val scale = remember { mutableStateOf(1f) }
    val transformableState = rememberTransformableState { zoomChange, _, _ ->
        scale.value = (scale.value * zoomChange).coerceIn(0.5f, 3f) // Limit zoom between 1x and 3x
    }

    // Container for the video player and control components
    Box(
        modifier = modifier
            .clipToBounds()
            .then(
                if (!isScreenLocked && playerHost.isZoomEnabled) Modifier.transformable(
                    transformableState
                )
                else Modifier
            )
            .pointerInput(Unit) {
                detectTapGestures { _ ->
                    onShowControlsToggle() // Toggle show/hide controls on tap
                    showSpeedSelection = false
                }
            }
    ) {
        // Video player component
        CMPPlayer(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = scale.value,
                    scaleY = scale.value,
                    transformOrigin = TransformOrigin.Center
                ),
            url = playerHost.url,
            isPause = playerHost.isPaused,
            isMute = playerHost.isMuted,
            totalTime = {
                playerHost.updateTotalTime(it)
            }, // Update total time of the video
            currentTime = {
                if (playerHost.isSliding.not()) {
                    playerHost.updateCurrentTime(it)  // Update current playback time
                    playerHost.seekToTime = null // Reset slider time if not sliding
                }
            },
            isSliding = playerHost.isSliding, // Pass seek bar sliding state
            seekToTime = playerHost.seekToTime, // Pass seek bar slider time
            speed = playerHost.speed, // Pass selected playback speed
            size = playerHost.videoFitMode,
            bufferCallback = { playerHost.setBufferingStatus(it) },
            didEndVideo = {
                playerHost.triggerMediaEnd()
                if (!playerHost.isLooping) {
                    playerHost.togglePlayPause()
                }
            },
            loop = playerHost.isLooping,
            volume = 0f,
            isLiveStream = playerConfig.isLiveStream,
            error = { playerHost.triggerError(it) },
            isHls = playerConfig.isHls
        )

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
                onResizeScreenToggle = {
                    playerHost.setVideoFitMode(
                        when (playerHost.videoFitMode) {
                            ScreenResize.FIT -> ScreenResize.FILL
                            ScreenResize.FILL -> ScreenResize.FIT
                        }
                    )
                },
                selectedSize = playerHost.videoFitMode
            )

            // Center control view for pause/resume and fast forward/backward actions
            CenterControls(
                playerConfig = playerConfig,
                isPause = playerHost.isPaused,
                onPauseToggle = {
                    playerHost.togglePlayPause()
                },
                onBackwardToggle = { // Seek backward
                    playerHost.isSliding = true
                    playerHost.seekToTime =
                        maxOf(
                            0,
                            playerHost.currentTime - playerConfig.fastForwardBackwardIntervalSeconds
                        )
                    playerHost.isSliding = false
                },
                onForwardToggle = { // Seek forward
                    playerHost.isSliding = true
                    playerHost.seekToTime = minOf(
                        playerHost.totalTime,
                        playerHost.currentTime + playerConfig.fastForwardBackwardIntervalSeconds
                    )
                    playerHost.isSliding = false
                },
                showControls = showControls,
            )

            if (playerConfig.isLiveStream) {
                LiveStreamComposable(playerConfig)
            } else {
                BottomControls(
                    playerConfig = playerConfig,
                    currentTime = playerHost.currentTime, // Pass current playback time
                    totalTime = playerHost.totalTime, // Pass total duration of the video
                    showControls = showControls, // Pass show/hide controls state
                    onChangeSliderTime = {
                        playerHost.seekToTime = it
                    }, // Update seek bar slider time
                    onChangeCurrentTime = { playerHost.updateCurrentTime(it) }, // Update current playback time
                    onChangeSliding = { // Update seek bar sliding state
                        playerHost.isSliding = it
                    }
                )
            }
        } else {
            if (playerConfig.isScreenLockEnabled) {
                LockScreenComponent(
                    playerConfig = playerConfig,
                    showControls = showControls,
                    onLockScreenToggle = { isScreenLocked = isScreenLocked.not() }
                )
            }
        }
        if (playerHost.isBuffering) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (playerConfig.loaderView != null) {
                    playerConfig.loaderView?.invoke()
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                            .size(playerConfig.pauseResumeIconSize),
                        color = playerConfig.loadingIndicatorColor
                    )
                }
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

