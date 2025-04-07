package uz.mobile.joybox.presentation


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import cafe.adriel.voyager.navigator.internal.BackHandler
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import cafe.adriel.voyager.transitions.SlideTransition
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import org.koin.compose.koinInject
import uz.mobile.joybox.datastore.CloseApp
import uz.mobile.joybox.presentation.components.getAsyncImageLoader
import uz.mobile.joybox.presentation.platform.MoviesAppScreen
import uz.mobile.joybox.presentation.screens.auth.login.LoginScreen
import uz.mobile.joybox.presentation.screens.onboarding.OnboardingScreen
import uz.mobile.joybox.presentation.screens.pricing.PricingPlanScreen
import uz.mobile.joybox.presentation.sharedviewmodel.FullScreenStateModel
import uz.mobile.joybox.presentation.tabs.HomeTab
import uz.mobile.joybox.presentation.tabs.LiveTab
import uz.mobile.joybox.presentation.tabs.ProfileTab
import uz.mobile.joybox.presentation.tabs.SearchTab
import uz.mobile.joybox.presentation.theme.MoviesAppTheme
import uz.mobile.joybox.presentation.theme.backgroundDark
import uz.mobile.joybox.presentation.util.ChangeStatusBarColors
import uz.mobile.joybox.presentation.util.getNavigationBarHeight
import uz.mobile.joybox.presentation.util.setFullScreen

@OptIn(ExperimentalCoilApi::class)
@Composable
fun App(
    toggleSplashScreen: (Boolean) -> Unit
) {


    setSingletonImageLoaderFactory { context ->
        getAsyncImageLoader(context)
    }

    MoviesAppTheme {
        ChangeStatusBarColors(Color.Transparent)
        val model: AppScreenModel = koinInject()

        val stateChecking = model.stateChecking

        LaunchedEffect(stateChecking) {
            toggleSplashScreen(stateChecking)
        }

        Box(
            modifier = Modifier.background(backgroundDark).fillMaxSize()
        ) {

            when (model.navigate) {
                Navigate.HOME -> navigateToApplication()
                Navigate.ONBOARDING -> navigateToOnboardingScreen()
                Navigate.LOGIN -> navigateToLoginScreen()
                Navigate.Idle -> {}
            }

        }

    }
}

@Composable
private fun navigateToPricingPlanScreen() {
    Navigator(PricingPlanScreen()) { navigator ->
        SlideTransition(navigator)
    }
}

@OptIn(ExperimentalMaterialApi::class, InternalVoyagerApi::class)
@Composable
private fun navigateToApplication() {
    TabNavigator(HomeTab) {
        HandleBackAction()
        BottomSheetNavigator(
            modifier = Modifier,
            sheetBackgroundColor = backgroundDark
        ) {
            Navigator(Application()) { navigator ->
                SlideTransition(navigator)
            }
        }
    }
}

@OptIn(InternalVoyagerApi::class)
@Composable
fun HandleBackAction() {
    val tabNavigator = LocalTabNavigator.current
    var checkClose by remember { mutableStateOf(false) }
    var close: Unit? = null
    if (checkClose) close = CloseApp {}
    BackHandler(true, onBack = {
        if (tabNavigator.current != HomeTab) {
            tabNavigator.current = HomeTab
        } else {
            if (checkClose) close else checkClose = true
        }
    })
}

@Composable
private fun navigateToOnboardingScreen() {
    Navigator(OnboardingScreen()) { navigator ->
        SlideTransition(navigator)
    }
}


@Composable
private fun navigateToLoginScreen() {
    Navigator(LoginScreen()) { navigator ->
        SlideTransition(navigator)
    }
}


class Application : MoviesAppScreen {

    @Composable
    override fun Content() {
        val fullScreenStateModel: FullScreenStateModel = koinScreenModel()
        val isFullScreen = fullScreenStateModel.isFullScreen

        val height = (75 + getNavigationBarHeight()).dp

        LaunchedEffect(isFullScreen) {
            setFullScreen(isFullScreen)
        }

        val liveTab = remember { LiveTab() }

        Scaffold(
            modifier = Modifier,
            bottomBar = {

                AnimatedVisibility(visible = !isFullScreen, enter = slideInVertically { height ->
                    height
                }, exit = slideOutVertically { height ->
                    height
                }) {
                    NavigationBar(
                        modifier = Modifier.fillMaxWidth().height(height),
                        containerColor = Color(0xFF191B1C)
                    ) {
                        TabNavigationItem(tab = HomeTab)
                        TabNavigationItem(tab = SearchTab)
                        TabNavigationItem(tab = liveTab)
                        TabNavigationItem(tab = ProfileTab)
                    }
                }
            },
        ) {

            CurrentTab()
        }


    }
}

//@Composable
//fun CurrentTabContent() {
//    when (LocalTabNavigator.current.current) {
//        is HomeTab -> TabWithResettableNavigator(tab = HomeTab, startScreen = HomeScreen())
//        is SearchTab -> TabWithResettableNavigator(tab = SearchTab, startScreen = SearchScreenContent())
//        is LiveTab -> TabWithResettableNavigator(tab = LiveTab, startScreen = LiveScreen())
//        is ProfileTab -> TabWithResettableNavigator(tab = ProfileTab, startScreen = ProfileScreen())
//    }
//}

@Composable
fun TabWithResettableNavigator(tab: Tab, startScreen: Screen) {
    val tabNavigator = LocalTabNavigator.current
    Navigator(startScreen) { navigator ->
        LaunchedEffect(tabNavigator.current) {
            if (tabNavigator.current != tab) {
                navigator.replaceAll(startScreen)
            }
        }
        SlideTransition(navigator) {
            it.Content()
        }
    }
}


@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current

    NavigationBarItem(
        modifier = Modifier.background(Color.Transparent),
        alwaysShowLabel = false,
        selected = tabNavigator.current.key == tab.key,
        onClick = { tabNavigator.current = tab },
        icon = {
            tab.options.icon?.let {
                Icon(
                    painter = tab.options.icon!!,
                    contentDescription = tab.options.title,
                    modifier = Modifier.size(22.dp)
                )
            }
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color.White,
            unselectedIconColor = Color.Gray,
            indicatorColor = Color.Transparent,
        )
    )
}
