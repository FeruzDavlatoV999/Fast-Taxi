import androidx.compose.runtime.Composable
import uz.mobile.taxi.domain.util.camera.SharedImage

@Composable
expect fun rememberCameraManager(onResult: (SharedImage?) -> Unit): CameraManager


expect class CameraManager(
    onLaunch: () -> Unit
) {
    fun launch()
}