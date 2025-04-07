package uz.mobile.joybox.presentation.tabs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideTransition
import joybox.composeapp.generated.resources.*
import org.jetbrains.compose.resources.vectorResource
import uz.mobile.joybox.presentation.screens.profile.ProfileScreen

object ProfileTab : Tab {

    @Composable
    override fun Content() {
        Navigator(screen = ProfileScreen()){ navigator->
            SlideTransition(navigator = navigator)
        }
    }

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(vectorResource(Res.drawable.ic_profile))
            return remember { TabOptions(index = 0u, title = "Profile", icon = icon) }
        }
}