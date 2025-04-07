package uz.mobile.joybox.presentation.screens.profile.settings.changePassword.forgotPasswordChange

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import chaintech.network.connectivitymonitor.ConnectivityStatus
import kotlinx.coroutines.flow.collectLatest
import joybox.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import uz.mobile.joybox.datastore.changeLang
import uz.mobile.joybox.domain.model.User
import uz.mobile.joybox.presentation.NetworkScreenModel
import uz.mobile.joybox.presentation.components.AuthTopBar
import uz.mobile.joybox.presentation.components.InternetOffline
import uz.mobile.joybox.presentation.components.MainButton
import uz.mobile.joybox.presentation.components.PasswordTextField
import uz.mobile.joybox.presentation.components.showToastMsg
import uz.mobile.joybox.presentation.platform.MoviesAppScreen
import uz.mobile.joybox.presentation.screens.profile.settings.changePassword.forgotPasswordOtp.ProfileForgetPasswordOtpScreen
import uz.mobile.joybox.presentation.screens.profile.settings.language.LanguageViewModel
import uz.mobile.joybox.presentation.theme.getTypography

class ProfileForgotPasswordChangePasswordScreen(val data: User) : MoviesAppScreen {

    @Composable
    override fun Content() {
        val networkScreenModel = remember { NetworkScreenModel() }
        val connectivityStatus by networkScreenModel.connectivityStatus.collectAsState()

        when (connectivityStatus) {
            ConnectivityStatus.NOT_CONNECTED -> InternetOffline(tryAgain = { networkScreenModel.refresh() })
            else -> {
                ForgotPasswordChangePasswordScreenContent()
            }
        }
    }


    @Composable
    fun ForgotPasswordChangePasswordScreenContent(
        navigator: Navigator = LocalNavigator.currentOrThrow,
        viewModel: ProfileForgotPasswordChangePasswordViewModel = koinScreenModel(),
        languageViewModel: LanguageViewModel = koinInject(),
    ) {

        val state = viewModel.state
        val language = languageViewModel.language

        LaunchedEffect(language) {
            languageViewModel.language.collectLatest {
                languageViewModel.selectedLanguage = it
            }
        }

        LaunchedEffect(state) {
            if (state is ProfileForgotPasswordChangePasswordState.Success) {
                navigator.push(ProfileForgetPasswordOtpScreen(data = data.copy(password = state.password)))
                viewModel.changeState()
            } else if (state is ProfileForgotPasswordChangePasswordState.Error) {
                showToastMsg(state.error)
                viewModel.changeState()
            }
        }

        key(languageViewModel.selectedLanguage) {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp).windowInsetsPadding(WindowInsets.safeDrawing)
            ) {
                AuthTopBar(
                    onBackButtonClicked = {
                        navigator.pop()
                    }, onLanguageChanged = { language ->
                        languageViewModel.saveLanguage(language.lowercase())
                        changeLang(language.lowercase())
                        languageViewModel.selectedLanguage = language.lowercase()
                    }, selectedLanguage = languageViewModel.selectedLanguage
                )

                Text(
                    text = stringResource(Res.string.create_new_password),
                    style = getTypography().headlineSmall
                )

                Spacer(Modifier.fillMaxWidth().height(24.dp))

                PasswordTextField(
                    value = viewModel.password,
                    onValueChange = viewModel::updatePhone,
                    error = viewModel.errorPassword,
                    hint = stringResource(Res.string.password)
                )

                Spacer(Modifier.fillMaxWidth().height(20.dp))

                MainButton(
                    text = stringResource(Res.string.continuee),
                    isLoading = state is ProfileForgotPasswordChangePasswordState.Loading
                ) {
                    viewModel.onValidate(user = data)
                }

            }
        }

    }

}