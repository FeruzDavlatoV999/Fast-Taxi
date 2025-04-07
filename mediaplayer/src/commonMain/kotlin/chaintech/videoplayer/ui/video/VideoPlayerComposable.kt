package chaintech.videoplayer.ui.video

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import chaintech.videoplayer.host.MediaPlayerHost
import chaintech.videoplayer.model.VideoPlayerConfig
import chaintech.videoplayer.ui.youtube.YoutubePlayerWithControl
import chaintech.videoplayer.util.LandscapeOrientation
import chaintech.videoplayer.util.extractYouTubeVideoId
import kotlinx.coroutines.delay

@Composable
fun VideoPlayerComposable(
    modifier: Modifier = Modifier, // Modifier for the composable
    playerHost: MediaPlayerHost,
    playerConfig: VideoPlayerConfig = VideoPlayerConfig(),
) {
    var showControls by remember { mutableStateOf(playerConfig.showControls) } // State for showing/hiding controls

    if (playerConfig.showControls && playerConfig.isAutoHideControlEnabled) {
        LaunchedEffect(showControls) {
            if (showControls) {
                delay(timeMillis = (playerConfig.controlHideIntervalSeconds * 1000).toLong()) // Delay hiding controls
                if (!playerHost.isSliding) {
                    showControls = false // Hide controls if seek bar is not being slid
                }
            }
        }
    }

    val videoUrl = extractYouTubeVideoId(playerHost.url)

    LandscapeOrientation(playerHost.isFullScreen) {
        if (videoUrl == null) {
            VideoPlayerWithControl(
                modifier = if (playerHost.isFullScreen) {
                    Modifier.fillMaxSize()
                } else {
                    modifier
                },
                playerHost = playerHost,
                playerConfig = playerConfig, // Player configuration
                showControls = showControls, // Flag indicating if controls should be shown
                onShowControlsToggle = {
                    if (playerConfig.showControls) {
                        showControls = showControls.not()
                    }
                } // Toggle show/hide controls
            )

        } else {

            YoutubePlayerWithControl(
                modifier = if (playerHost.isFullScreen) {
                    Modifier.fillMaxSize()
                } else {
                    modifier
                },
                playerHost = playerHost,
                playerConfig = playerConfig, // Player configuration
                showControls = showControls, // Flag indicating if controls should be shown
                onShowControlsToggle = {
                    if (playerConfig.showControls) {
                        showControls = showControls.not()
                    }
                }// Toggle show/hide controls

            )
        }
    }
}








