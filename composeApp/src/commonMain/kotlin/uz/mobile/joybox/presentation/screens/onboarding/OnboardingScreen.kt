package uz.mobile.joybox.presentation.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.flow.collectLatest
import joybox.composeapp.generated.resources.Res
import joybox.composeapp.generated.resources.login
import joybox.composeapp.generated.resources.onboarding_background
import joybox.composeapp.generated.resources.onboarding_description
import joybox.composeapp.generated.resources.onboarding_logo
import joybox.composeapp.generated.resources.onboarding_welcome
import joybox.composeapp.generated.resources.register
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import uz.mobile.joybox.datastore.changeLang
import uz.mobile.joybox.presentation.components.LanguageDropdownFilled
import uz.mobile.joybox.presentation.components.MainButton
import uz.mobile.joybox.presentation.components.SecondaryButton
import uz.mobile.joybox.presentation.platform.MoviesAppScreen
import uz.mobile.joybox.presentation.screens.auth.login.LoginScreen
import uz.mobile.joybox.presentation.screens.auth.registerName.RegisterNameScreen
import uz.mobile.joybox.presentation.screens.profile.settings.language.LanguageViewModel
import uz.mobile.joybox.presentation.theme.getTypography

class OnboardingScreen : MoviesAppScreen {

    @Composable
    override fun Content() {
        OnboardingContent()
    }


    @Composable
    fun OnboardingContent(
        navigator: Navigator = LocalNavigator.currentOrThrow,
        languageViewModel: LanguageViewModel = koinInject(),
    ) {

        LaunchedEffect(languageViewModel.language) {
            languageViewModel.language.collectLatest {
                languageViewModel.selectedLanguage = it
                changeLang(it)
            }
        }

        key(languageViewModel.selectedLanguage) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Black),
            ) {
                Image(
                    painter = painterResource(Res.drawable.onboarding_background),
                    contentDescription = "background",
                    modifier = Modifier.fillMaxSize().offset(y = (-72).dp),
                    contentScale = ContentScale.Crop,
                )

                LanguageDropdownFilled(
                    modifier = Modifier.align(Alignment.TopEnd).windowInsetsPadding(WindowInsets.safeDrawing).padding(top = 20.dp, end = 18.dp),
                    selectedLanguage = languageViewModel.selectedLanguage,
                    onLanguageSelected = {
                        languageViewModel.saveLanguage(it)
                        changeLang(it.lowercase())
                        languageViewModel.selectedLanguage = it.lowercase()
                    }
                )

                Box(
                    modifier = Modifier.fillMaxSize().background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF111111).copy(alpha = 0.0f),
                                Color(0xFF111111).copy(alpha = 0.45f),
                                Color(0xFF111111).copy(alpha = 0.85f),
                                Color(0xFF111111),
                                Color(0xFF111111)
                            )
                        )
                    )
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(16.dp).align(Alignment.BottomEnd),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Spacer(Modifier.fillMaxWidth().weight(4f))

                        Image(
                            painter = painterResource(Res.drawable.onboarding_logo),
                            contentDescription = "logo",
                            modifier = Modifier.height(185.dp).width(145.dp)
                        )

                        Spacer(Modifier.fillMaxWidth().weight(1f))

                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(Res.string.onboarding_welcome),
                            style = getTypography().headlineMedium,
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.fillMaxWidth().height(12.dp))

                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(Res.string.onboarding_description),
                            style = getTypography().bodyLarge,
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.fillMaxWidth().height(40.dp))

                        MainButton(
                            modifier = Modifier.fillMaxWidth().height(44.dp),
                            text = stringResource(Res.string.login)
                        ) {
                            navigator.push(LoginScreen())
                        }

                        Spacer(Modifier.fillMaxWidth().height(10.dp))

                        SecondaryButton(
                            modifier = Modifier.fillMaxWidth().height(44.dp),
                            text = stringResource(Res.string.register)
                        ) {
                            navigator.push(RegisterNameScreen())
                        }

                        Spacer(Modifier.fillMaxWidth().height(56.dp))
                    }
                }
            }
        }

    }


    @Preview
    @Composable
    fun OnboardingContentPreview() {
        OnboardingContent()
    }
}

