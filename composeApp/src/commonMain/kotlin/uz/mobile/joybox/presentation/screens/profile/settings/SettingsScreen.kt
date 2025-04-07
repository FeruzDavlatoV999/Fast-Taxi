package uz.mobile.joybox.presentation.screens.profile.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import chaintech.network.connectivitymonitor.ConnectivityStatus
import joybox.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import uz.mobile.joybox.presentation.NetworkScreenModel
import uz.mobile.joybox.presentation.components.InternetOffline
import uz.mobile.joybox.presentation.components.MainTopBar
import uz.mobile.joybox.presentation.components.NotificationToggle
import uz.mobile.joybox.presentation.components.ProfileSections
import uz.mobile.joybox.presentation.platform.MoviesAppScreen
import uz.mobile.joybox.presentation.screens.naviagationActions.navigateToProfileItemsScreen
import uz.mobile.joybox.presentation.screens.profile.ProfileViewModel
import uz.mobile.joybox.presentation.screens.profile.settings.changePassword.forgotPasswordPhone.ProfileForgotPasswordPhoneScreen


class SettingsScreen : MoviesAppScreen {
    @Composable
    override fun Content() {
        val networkScreenModel = remember { NetworkScreenModel() }
        val connectivityStatus by networkScreenModel.connectivityStatus.collectAsState()
        when(connectivityStatus){
            ConnectivityStatus.NOT_CONNECTED -> InternetOffline(tryAgain = { networkScreenModel.refresh() })
            else -> {Setting()}
        }
    }

}

@Composable
fun Setting(
    navigator: Navigator = LocalNavigator.currentOrThrow,
    viewModel: ProfileViewModel = koinInject(),
) {

    LaunchedEffect(Unit) {
        viewModel.notification.collect {
            viewModel.isChecked = it
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        MainTopBar(onBackButtonClicked = {
            navigator.pop()
        }, text = "Profile")

        NotificationToggle(viewModel.isChecked) {
            viewModel.saveNotification(it)
            viewModel.isChecked = it
        }

        ProfileSections(title = stringResource(Res.string.select_language), isIconVisible = true, onClick = {
            navigateToProfileItemsScreen(navigator = navigator)
        })

        ProfileSections(title = stringResource(Res.string.change_password), isIconVisible = true, onClick = {
            navigator.push(ProfileForgotPasswordPhoneScreen())
        })
    }
}