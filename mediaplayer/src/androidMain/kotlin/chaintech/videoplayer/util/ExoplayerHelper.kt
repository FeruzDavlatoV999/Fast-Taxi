package chaintech.videoplayer.util

import android.content.Context
import android.net.Uri
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import chaintech.videoplayer.host.MediaPlayerError

@OptIn(UnstableApi::class)
@Composable
fun rememberPlayerView(exoPlayer: ExoPlayer, context: Context): PlayerView {
    val playerView = remember(context) {
        PlayerView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            useController = false
            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            player = exoPlayer
            setShowBuffering(PlayerView.SHOW_BUFFERING_NEVER)
        }
    }
    DisposableEffect(playerView) {
        onDispose {
            playerView.player = null
        }
    }
    return playerView
}

@OptIn(UnstableApi::class)
@Composable
fun rememberExoPlayerWithLifecycle(
    url: String,
    context: Context,
    isPause: Boolean,
    isLiveStream: Boolean,
    error: (MediaPlayerError) -> Unit,
    isHls: Boolean
): ExoPlayer {
    val lifecycleOwner = LocalLifecycleOwner.current
    // Create cache instance
    val cache = CacheManager.getCache(context)

    val exoPlayer = remember(context) {
        ExoPlayer.Builder(context).build()
            .apply {
                videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
                repeatMode = Player.REPEAT_MODE_OFF
                setHandleAudioBecomingNoisy(true)
            }
    }
    LaunchedEffect(url) {
        try {
            val videoUri = Uri.parse(url)
            val mediaItem = MediaItem.fromUri(videoUri)

            // Reset the player to the start
            exoPlayer.seekTo(0, 0)

            // Prepare the appropriate media source based on the URL type
            val mediaSource = if (isLiveStream || isHls) {
                createHlsMediaSource(mediaItem)
            } else {
                createProgressiveMediaSource(mediaItem, context, cache)
            }

            exoPlayer.setMediaSource(mediaSource)
            exoPlayer.prepare()
        } catch (e: Exception) {
            error(MediaPlayerError.PlaybackError(e.message ?: "Failed to load media"))
        }
    }

    var appInBackground by remember {
        mutableStateOf(false)
    }

    DisposableEffect(key1 = lifecycleOwner, appInBackground) {
        val lifecycleObserver =
            getExoPlayerLifecycleObserver(exoPlayer, isPause, appInBackground) {
                appInBackground = it
            }
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }
    return exoPlayer
}

@OptIn(UnstableApi::class)
private fun createHlsMediaSource(mediaItem: MediaItem): MediaSource {
    val dataSourceFactory = DefaultHttpDataSource.Factory().setAllowCrossProtocolRedirects(true)
    return HlsMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
}

@OptIn(UnstableApi::class)
private fun createProgressiveMediaSource(
    mediaItem: MediaItem,
    context: Context,
    cache: Cache
): MediaSource {
    val dataSourceFactory = CacheDataSource.Factory()
        .setCache(cache)
        .setUpstreamDataSourceFactory(DefaultDataSource.Factory(context))
        .setFlags(CacheDataSource.FLAG_BLOCK_ON_CACHE or CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    return ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
}