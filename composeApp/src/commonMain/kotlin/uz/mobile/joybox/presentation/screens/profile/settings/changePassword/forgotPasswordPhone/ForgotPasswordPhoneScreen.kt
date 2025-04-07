package uz.mobile.joybox.presentation.screens.profile.settings.changePassword.forgotPasswordPhone

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
import uz.mobile.joybox.presentation.components.PhoneInputTextField
import uz.mobile.joybox.presentation.components.PhoneNumberElement
import uz.mobile.joybox.presentation.platform.MoviesAppScreen
import uz.mobile.joybox.presentation.screens.profile.settings.changePassword.forgotPasswordChange.ProfileForgotPasswordChangePasswordScreen
import uz.mobile.joybox.presentation.screens.profile.settings.language.LanguageViewModel
import uz.mobile.joybox.presentation.theme.getTypography

class ProfileForgotPasswordPhoneScreen : MoviesAppScreen {

    @Composable
    override fun Content() {
        val networkScreenModel = remember { NetworkScreenModel() }
        val connectivityStatus by networkScreenModel.connectivityStatus.collectAsState()
        when (connectivityStatus) {
            ConnectivityStatus.NOT_CONNECTED -> InternetOffline(tryAgain = { networkScreenModel.refresh() })
            else -> {
                ForgotPasswordPhoneScreenContent()
            }
        }

    }


    @Composable
    fun ForgotPasswordPhoneScreenContent(
        navigator: Navigator = LocalNavigator.currentOrThrow,
        viewModel: ProfileForgotPasswordPhoneViewModel = koinScreenModel(),
        languageViewModel: LanguageViewModel = koinInject(),
    ) {

        val phoneState = viewModel.state
        val language = languageViewModel.language


        LaunchedEffect(language) {
            languageViewModel.language.collectLatest {
                languageViewModel.selectedLanguage = it
            }
        }

        LaunchedEffect(phoneState) {
            if (phoneState is ProfilePhoneForgotPasswordPhoneState.Success) {
                navigator.push(ProfileForgotPasswordChangePasswordScreen(data = User(phone = phoneState.phone)))
                viewModel.changeState()
            }
        }

        key(languageViewModel.selectedLanguage) {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp).windowInsetsPadding(WindowInsets.safeDrawing)
            ) {
                AuthTopBar(onBackButtonClicked = {
                    navigator.pop()
                }, selectedLanguage = languageViewModel.selectedLanguage,
                    onLanguageChanged = { language ->
                        languageViewModel.saveLanguage(language.lowercase())
                        changeLang(language.lowercase())
                        languageViewModel.selectedLanguage = language.lowercase()
                    })

                Text(
                    text = stringResource(Res.string.your_phone_number),
                    style = getTypography().headlineSmall
                )

                Spacer(Modifier.fillMaxWidth().height(8.dp))

                Text(
                    text = stringResource(Res.string.your_phone_number_description),
                    style = getTypography().bodyMedium
                )

                Spacer(Modifier.fillMaxWidth().height(24.dp))

                PhoneInputTextField(
                    config = PhoneNumberElement.FormatPatterns.UZB,
                    value = viewModel.phone,
                    onValueChange = viewModel::updatePhone,
                    error = viewModel.errorPhone
                )

                Spacer(Modifier.fillMaxWidth().height(20.dp))

                MainButton(
                    text = stringResource(Res.string.continuee),
                    isLoading = phoneState is ProfilePhoneForgotPasswordPhoneState.Loading
                ) {
                    viewModel.onValidate()
                }

            }
        }
    }

}