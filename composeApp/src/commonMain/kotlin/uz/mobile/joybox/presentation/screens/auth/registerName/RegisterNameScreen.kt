package uz.mobile.joybox.presentation.screens.auth.registerName

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import chaintech.network.connectivitymonitor.ConnectivityStatus
import kotlinx.coroutines.flow.collectLatest
import joybox.composeapp.generated.resources.Res
import joybox.composeapp.generated.resources.continuee
import joybox.composeapp.generated.resources.do_you_have_account
import joybox.composeapp.generated.resources.enter_account
import joybox.composeapp.generated.resources.enter_name_description
import joybox.composeapp.generated.resources.name
import joybox.composeapp.generated.resources.what_is_your_name
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import uz.mobile.joybox.datastore.changeLang
import uz.mobile.joybox.domain.model.User
import uz.mobile.joybox.presentation.NetworkScreenModel
import uz.mobile.joybox.presentation.components.AuthTopBar
import uz.mobile.joybox.presentation.components.InputTextField
import uz.mobile.joybox.presentation.components.InternetOffline
import uz.mobile.joybox.presentation.components.MainButton
import uz.mobile.joybox.presentation.platform.MoviesAppScreen
import uz.mobile.joybox.presentation.screens.auth.login.LoginScreen
import uz.mobile.joybox.presentation.screens.auth.registerPassword.RegisterPasswordScreen
import uz.mobile.joybox.presentation.screens.profile.settings.language.LanguageViewModel
import uz.mobile.joybox.presentation.theme.getTypography
import uz.mobile.joybox.presentation.theme.hintTextColorDark
import uz.mobile.joybox.presentation.theme.primaryLight

class RegisterNameScreen : MoviesAppScreen {

    @Composable
    override fun Content() {
        val networkScreenModel = remember { NetworkScreenModel() }
        val connectivityStatus by networkScreenModel.connectivityStatus.collectAsState()
        when (connectivityStatus) {
            ConnectivityStatus.NOT_CONNECTED -> InternetOffline(tryAgain = { networkScreenModel.refresh() })
            else -> {
                RegisterNameScreenContent()
            }
        }
    }

    @Composable
    fun RegisterNameScreenContent(
        navigator: Navigator = LocalNavigator.currentOrThrow,
        viewModel: RegisterNameViewModel = koinScreenModel(),
        languageViewModel: LanguageViewModel = koinInject(),
    ) {

        val nameState = viewModel.state

        val language = languageViewModel.language

        LaunchedEffect(language) {
            languageViewModel.language.collectLatest {
                languageViewModel.selectedLanguage = it
            }
        }

        LaunchedEffect(nameState) {
            if (nameState is NameState.Success) {
                navigator.push(RegisterPasswordScreen(User().copy(firstname = nameState.name)))
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
                    },
                    selectedLanguage = languageViewModel.selectedLanguage,
                    onLanguageChanged = { language ->
                        languageViewModel.saveLanguage(language.lowercase())
                        changeLang(language.lowercase())
                        languageViewModel.selectedLanguage = language.lowercase()
                    }
                )

                Text(
                    text = stringResource(Res.string.what_is_your_name),
                    style = getTypography().headlineSmall
                )

                Spacer(Modifier.fillMaxWidth().height(8.dp))

                Text(
                    text = stringResource(Res.string.enter_name_description),
                    style = getTypography().bodyMedium
                )

                Spacer(Modifier.fillMaxWidth().height(24.dp))


                InputTextField(
                    modifier = Modifier,
                    value = viewModel.name,
                    onValueChange = viewModel::updateName,
                    error = viewModel.errorName,
                    placeholder = {
                        Text(
                            text = stringResource(Res.string.name),
                            style = getTypography().bodyMedium.copy(color = hintTextColorDark)
                        )
                    }
                )



                Spacer(Modifier.fillMaxWidth().height(24.dp))

                MainButton(modifier = Modifier.height(41.dp),text = stringResource(Res.string.continuee), onClicked = viewModel::onNext)

                Spacer(Modifier.fillMaxWidth().height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(Res.string.do_you_have_account),
                        style = getTypography().labelSmall
                    )
                    Spacer(Modifier.width(6.dp))

                    Text(
                        modifier = Modifier.clickable {
                            navigator.push(LoginScreen())
                        },
                        text = stringResource(Res.string.enter_account),
                        style = getTypography().labelMedium.copy(color = primaryLight, fontWeight = FontWeight.Bold),
                    )
                }

            }
        }
    }

}