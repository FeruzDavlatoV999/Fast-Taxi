package uz.mobile.joybox.presentation.screens.auth.registerOtp

import ContentWithMessageBar
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import chaintech.network.connectivitymonitor.ConnectivityStatus
import kotlinx.coroutines.flow.collectLatest
import joybox.composeapp.generated.resources.Res
import joybox.composeapp.generated.resources.continuee
import joybox.composeapp.generated.resources.enter_the_code
import joybox.composeapp.generated.resources.enter_the_code_description
import joybox.composeapp.generated.resources.incorrect
import joybox.composeapp.generated.resources.not_received
import joybox.composeapp.generated.resources.resend
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import rememberMessageBarState
import uz.mobile.joybox.datastore.changeLang
import uz.mobile.joybox.domain.model.User
import uz.mobile.joybox.domain.util.UniversalText
import uz.mobile.joybox.presentation.NetworkScreenModel
import uz.mobile.joybox.presentation.components.AuthTopBar
import uz.mobile.joybox.presentation.components.InternetOffline
import uz.mobile.joybox.presentation.components.MainButton
import uz.mobile.joybox.presentation.components.OtpTextField
import uz.mobile.joybox.presentation.components.TimerWithResendButton
import uz.mobile.joybox.presentation.components.showToastMsg
import uz.mobile.joybox.presentation.platform.MoviesAppScreen
import uz.mobile.joybox.presentation.screens.pricing.PricingPlanScreen
import uz.mobile.joybox.presentation.screens.profile.settings.language.LanguageViewModel
import uz.mobile.joybox.presentation.theme.errorDark
import uz.mobile.joybox.presentation.theme.getTypography

class RegisterOtpScreen(val userData: User) : MoviesAppScreen {

    @Composable
    override fun Content() {
        val networkScreenModel = remember { NetworkScreenModel() }
        val connectivityStatus by networkScreenModel.connectivityStatus.collectAsState()
        when (connectivityStatus) {
            ConnectivityStatus.NOT_CONNECTED -> InternetOffline(tryAgain = { networkScreenModel.refresh() })
            else -> {
                RegisterOtpScreenContent()
            }
        }
    }

    @Composable
    fun RegisterOtpScreenContent(
        navigator: Navigator = LocalNavigator.currentOrThrow,
        viewModel: RegisterOtpScreenViewModel = koinScreenModel(),
        languageViewModel: LanguageViewModel = koinInject(),
    ) {

        val otpState = viewModel.state
        val language = languageViewModel.language

        val messageBarState = rememberMessageBarState()

        LaunchedEffect(language) {
            languageViewModel.language.collectLatest {
                languageViewModel.selectedLanguage = it
            }
        }

        LaunchedEffect(otpState) {
            if (otpState is OtpState.Success) {
                navigator.push(PricingPlanScreen())
                viewModel.changeState()
            }
        }

        if (otpState is OtpState.Error) {
            showToastMsg(otpState.error.asString())
            viewModel.changeState()
        }




        LaunchedEffect(Unit) {
            viewModel.startTimer()
        }

        key(languageViewModel.selectedLanguage) {
            ContentWithMessageBar(messageBarState = messageBarState) {
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
                        text = stringResource(Res.string.enter_the_code),
                        style = getTypography().headlineSmall
                    )

                    Spacer(Modifier.fillMaxWidth().height(8.dp))

                    Text(
                        text = stringResource(Res.string.enter_the_code_description),
                        style = getTypography().bodyMedium
                    )

                    Spacer(Modifier.fillMaxWidth().height(16.dp))

                    OtpTextField(
                        otpText = viewModel.otp,
                        onOtpTextChange = viewModel::updateOtp,
                        isIncorrect = viewModel.errorOtp !is UniversalText.Empty
                    )

                    Spacer(Modifier.fillMaxWidth().height(24.dp))


                    if (viewModel.errorOtp !is UniversalText.Empty) {
                        FinishRow(
                            modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                            string = Res.string.incorrect
                        ) {
                            viewModel.resendOtp(userData.phone.orEmpty())
                        }
                    } else if (viewModel.timer == 0) {
                        FinishRow(
                            modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                            string = Res.string.not_received
                        ) {
                            viewModel.resendOtp(userData.phone.orEmpty())
                        }
                    } else {
                        TimerWithResendButton(
                            time = viewModel.time,
                            onResendClick = { viewModel.resendOtp(userData.phone.orEmpty()) }
                        )
                    }

                    Spacer(Modifier.fillMaxWidth().height(24.dp))

                    MainButton(modifier = Modifier.height(45.dp), text = stringResource(Res.string.continuee), isLoading = otpState is OtpState.Loading) {
                        viewModel.register(userData)
                    }

                }
            }
        }
    }


    @Composable
    fun FinishRow(modifier: Modifier = Modifier, string: StringResource, onClicked: () -> Unit) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(string),
                style = getTypography().bodyMedium.copy(lineHeight = 21.sp, color = errorDark),
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = stringResource(Res.string.resend),
                style = getTypography().labelMedium.copy(lineHeight = 21.sp, color = errorDark),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.clickable(onClick = { onClicked() })
            )
        }
    }
}





