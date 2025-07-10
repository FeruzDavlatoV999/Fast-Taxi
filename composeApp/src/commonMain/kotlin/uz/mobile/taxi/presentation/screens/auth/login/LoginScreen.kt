package uz.mobile.taxi.presentation.screens.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import chaintech.network.connectivitymonitor.ConnectivityStatus
import kotlinx.coroutines.flow.collectLatest
import taxi.composeapp.generated.resources.Res
import taxi.composeapp.generated.resources.continuee
import taxi.composeapp.generated.resources.entrance
import taxi.composeapp.generated.resources.entrance_description
import taxi.composeapp.generated.resources.forgot_password
import taxi.composeapp.generated.resources.password
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import uz.mobile.taxi.datastore.changeLang
import uz.mobile.taxi.presentation.AppScreenModel
import uz.mobile.taxi.presentation.NetworkScreenModel
import uz.mobile.taxi.presentation.components.AuthTopBar
import uz.mobile.taxi.presentation.components.InternetOffline
import uz.mobile.taxi.presentation.components.MainButton
import uz.mobile.taxi.presentation.components.PasswordTextField
import uz.mobile.taxi.presentation.components.PhoneInputTextField
import uz.mobile.taxi.presentation.components.PhoneNumberElement
import uz.mobile.taxi.presentation.components.showToastMsg
import uz.mobile.taxi.presentation.platform.MoviesAppScreen
import uz.mobile.taxi.presentation.screens.auth.forgotPasswordPhone.ForgotPasswordPhoneScreen
import uz.mobile.taxi.presentation.screens.profile.settings.language.LanguageViewModel
import uz.mobile.taxi.presentation.theme.getTypography
import uz.mobile.taxi.presentation.theme.textButtonTextColorDark

class LoginScreen : MoviesAppScreen {

    @Composable
    override fun Content() {

        val networkScreenModel = remember { NetworkScreenModel() }
        val connectivityStatus by networkScreenModel.connectivityStatus.collectAsState()

        when (connectivityStatus) {
            ConnectivityStatus.NOT_CONNECTED -> InternetOffline(tryAgain = { networkScreenModel.refresh() })
            else -> {
                println("TTT  connectivityStatus = $connectivityStatus ")
                LoginScreenContent()
            }
        }
    }


    @Composable
    fun LoginScreenContent(
        navigator: Navigator = LocalNavigator.currentOrThrow,
        viewModel: LoginScreenViewModel = koinScreenModel(),
        languageViewModel: LanguageViewModel = koinInject(),
        appModel: AppScreenModel = koinInject(),
    ) {

        val state = viewModel.state
        val language = languageViewModel.language

        val snackbarHostState = remember { SnackbarHostState() }

        LaunchedEffect(language) {
            languageViewModel.language.collectLatest {
                languageViewModel.selectedLanguage = it
            }
        }

        LaunchedEffect(state) {
            if (state is LoginState.Success) {
                viewModel.changeState()
                appModel.navigateHome()
            }
        }

        if (state is LoginState.Error) {
            showToastMsg(msg = state.error.asString())
            viewModel.changeState()
        }



        Scaffold(
            backgroundColor = Color.Transparent,
            snackbarHost = {
                SnackbarHost(snackbarHostState)
            }
        ) {
            key(languageViewModel.selectedLanguage) {

                Column(
                    modifier = Modifier.padding(horizontal = 20.dp)
                        .windowInsetsPadding(WindowInsets.safeDrawing)
                ) {
                    AuthTopBar(
                        onBackButtonClicked = {
                            navigator.pop()
                        },
                        selectedLanguage = languageViewModel.selectedLanguage,
                        onLanguageChanged = { language ->
                            languageViewModel.saveLanguage(language.lowercase())
                            changeLang(language.lowercase())
                            languageViewModel.selectedLanguage = language.lowercase()
                        }
                    )

                    Text(
                        text = stringResource(Res.string.entrance),
                        textAlign = TextAlign.Start,
                        style = getTypography().headlineSmall
                    )

                    Spacer(Modifier.fillMaxWidth().height(8.dp))

                    Text(
                        text = stringResource(Res.string.entrance_description),
                        style = getTypography().bodyMedium
                    )

                    Spacer(Modifier.fillMaxWidth().height(16.dp))

                    PhoneInputTextField(
                        config = PhoneNumberElement.FormatPatterns.UZB,
                        value = viewModel.phone,
                        onValueChange = viewModel::updatePhone,
                        error = viewModel.errorPhone
                    )

                    Spacer(Modifier.fillMaxWidth().height(8.dp))

                    PasswordTextField(
                        value = viewModel.password,
                        onValueChange = viewModel::updatePassword,
                        modifier = Modifier.height(45.dp).fillMaxWidth(),
                        error = viewModel.errorPassword,
                        hint = stringResource(Res.string.password)
                    )

                    Spacer(Modifier.fillMaxWidth().height(20.dp))

                    MainButton(
                        modifier = Modifier.height(45.dp),
                        text = stringResource(Res.string.continuee),
                        isLoading = state is LoginState.Loading
                    ) {
                        viewModel.onLogin()
                    }

                    Spacer(Modifier.fillMaxWidth().height(8.dp))

                    TextButton(
                        modifier = Modifier.fillMaxWidth().background(color = Color.Transparent),
                        onClick = {
                            navigator.push(ForgotPasswordPhoneScreen())
                        }
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.CenterVertically),
                            text = stringResource(Res.string.forgot_password),
                            style = getTypography().bodySmall.copy(color = textButtonTextColorDark)
                        )
                    }
                }
            }
        }
    }

}