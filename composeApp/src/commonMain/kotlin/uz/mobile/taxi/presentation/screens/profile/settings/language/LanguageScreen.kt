package uz.mobile.taxi.presentation.screens.profile.settings.language

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.Divider
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import chaintech.network.connectivitymonitor.ConnectivityStatus
import kotlinx.coroutines.flow.collectLatest
import taxi.composeapp.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import uz.mobile.taxi.datastore.changeLang
import uz.mobile.taxi.presentation.NetworkScreenModel
import uz.mobile.taxi.presentation.components.InternetOffline
import uz.mobile.taxi.presentation.components.MainTopBar
import uz.mobile.taxi.presentation.platform.MoviesAppScreen


class LanguageScreen : MoviesAppScreen {
    @Composable
    override fun Content() {
        val networkScreenModel = remember { NetworkScreenModel() }
        val connectivityStatus by networkScreenModel.connectivityStatus.collectAsState()
        when(connectivityStatus){
            ConnectivityStatus.NOT_CONNECTED -> InternetOffline(tryAgain = { networkScreenModel.refresh() })
            else -> {
                Language()
            }
        }

    }

}

@Composable
fun Language(
    viewModel: LanguageViewModel = koinInject(),
    navigator: Navigator = LocalNavigator.currentOrThrow
) {
    var selectedLanguage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.language.collectLatest {
            selectedLanguage = it
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .background(Color(0xFF121212))
    ) {
        MainTopBar(onBackButtonClicked = {
            navigator.pop()
        }, text = stringResource(Res.string.language))




        key(selectedLanguage) {
            LanguageOptionItem(
                image = Res.drawable.flag_ru,
                language = stringResource(Res.string.russian_language),
                isSelected = selectedLanguage == "ru",
                onClick = {
                    selectedLanguage = "ru"
                    viewModel.saveLanguage("ru")
                    changeLang("ru")
                }
            )
            Divider(color = Color.Gray.copy(alpha = 0.5f))
            LanguageOptionItem(
                image = Res.drawable.flag_uzb,
                language = stringResource(Res.string.uzbek_language),
                isSelected = selectedLanguage == "uz",
                onClick = {
                    selectedLanguage = "uz"
                    viewModel.saveLanguage("uz")
                    changeLang("uz")
                }
            )
            Divider(color = Color.Gray.copy(alpha = 0.5f))
        }
    }
}

@Composable
fun LanguageOptionItem(
    image: DrawableResource,
    language: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(image),
            contentDescription = language,
            modifier = Modifier
                .size(32.dp).padding(start = 8.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = language,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f),
            style = TextStyle(
                lineHeight = 29.sp,
                fontFamily = FontFamily(Font(Res.font.inter_medium, FontWeight.Normal, FontStyle.Normal)),
                fontWeight = FontWeight(500)
            )
        )

        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color.White,
                unselectedColor = Color.Gray
            )
        )
    }
}
