package uz.mobile.taxi.presentation.screens.auth.registerPassword

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
import taxi.composeapp.generated.resources.Res
import taxi.composeapp.generated.resources.continuee
import taxi.composeapp.generated.resources.create_password
import taxi.composeapp.generated.resources.create_password_description
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import uz.mobile.taxi.datastore.changeLang
import uz.mobile.taxi.domain.model.User
import uz.mobile.taxi.presentation.NetworkScreenModel
import uz.mobile.taxi.presentation.components.AuthTopBar
import uz.mobile.taxi.presentation.components.InternetOffline
import uz.mobile.taxi.presentation.components.MainButton
import uz.mobile.taxi.presentation.components.PasswordTextField
import uz.mobile.taxi.presentation.platform.MoviesAppScreen
import uz.mobile.taxi.presentation.screens.auth.registerPhone.RegisterPhoneScreen
import uz.mobile.taxi.presentation.screens.profile.settings.language.LanguageViewModel
import uz.mobile.taxi.presentation.theme.getTypography

class RegisterPasswordScreen(
    val userData: User,
) : MoviesAppScreen {

    @Composable
    override fun Content() {
        val networkScreenModel = remember { NetworkScreenModel() }
        val connectivityStatus by networkScreenModel.connectivityStatus.collectAsState()
        when(connectivityStatus){
            ConnectivityStatus.NOT_CONNECTED -> InternetOffline(tryAgain = { networkScreenModel.refresh() })
            else -> {
                RegisterPasswordScreenContent()
            }
        }

    }


    @Composable
    fun RegisterPasswordScreenContent(
        navigator: Navigator = LocalNavigator.currentOrThrow,
        viewModel: RegisterPasswordViewModel = koinScreenModel(),
        languageViewModel: LanguageViewModel = koinInject(),
    ) {

        val passwordState = viewModel.state

        LaunchedEffect(languageViewModel.language) {
            languageViewModel.language.collectLatest {
                languageViewModel.selectedLanguage = it
                changeLang(it)
            }
        }

        LaunchedEffect(passwordState) {
            if (passwordState is PasswordState.Success) {
                navigator.push(RegisterPhoneScreen(userData = userData.copy(password = passwordState.password)))
                viewModel.changeState()
            }
        }


        key(languageViewModel.selectedLanguage){
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
                    text = stringResource(Res.string.create_password),
                    style = getTypography().headlineSmall
                )

                Spacer(Modifier.fillMaxWidth().height(8.dp))

                Text(
                    text = stringResource(Res.string.create_password_description),
                    style = getTypography().bodyMedium
                )

                Spacer(Modifier.fillMaxWidth().height(24.dp))

                PasswordTextField(
                    value = viewModel.password,
                    onValueChange = viewModel::updatePassword,
                    hint = stringResource(Res.string.create_password),
                    error = viewModel.errorPassword
                )

                Spacer(Modifier.fillMaxWidth().height(20.dp))

                MainButton(modifier = Modifier.height(41.dp),text = stringResource(Res.string.continuee)) {
                    viewModel.checkPassword()
                }

            }
        }

    }

}