package chaintech.videoplayer.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import chaintech.videoplayer.host.MediaPlayerError
import chaintech.videoplayer.model.PlayerSpeed
import chaintech.videoplayer.model.ScreenResize



@Composable
internal expect fun CMPPlayer(
    modifier: Modifier,
    url: String,
    isPause: Boolean,
    isMute: Boolean,
    totalTime: ((Int) -> Unit),
    currentTime: ((Int) -> Unit),
    isSliding: Boolean,
    seekToTime: Int?,
    speed: PlayerSpeed,
    size: ScreenResize,
    bufferCallback: ((Boolean) -> Unit),
    didEndVideo: (() -> Unit),
    loop: Boolean,
    volume: Float,
    isLiveStream: Boolean,
    error: (MediaPlayerError) -> Unit,
    isHls:Boolean
)

