package uz.mobile.joybox.presentation.tabs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import joybox.composeapp.generated.resources.*
import org.jetbrains.compose.resources.vectorResource
import uz.mobile.joybox.presentation.screens.live.LiveMainScreen
import uz.mobile.joybox.presentation.sharedviewmodel.FullScreenStateModel

class LiveTab: Tab {

    @Composable
    override fun Content() {
        val fullScreenStateModel: FullScreenStateModel = koinScreenModel()

        LiveMainScreen(
            fullScreenStateModel = fullScreenStateModel,
            showStatusBar = false,
        )
    }

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(vectorResource(Res.drawable.ic_player))
            return remember { TabOptions(index = 0u, title = "Play", icon = icon) }
        }

}
