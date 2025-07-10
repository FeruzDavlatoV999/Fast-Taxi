package uz.mobile.taxi.presentation.screens.profile.settings.privacy

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import taxi.composeapp.generated.resources.*
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import uz.mobile.taxi.presentation.NetworkScreenModel
import uz.mobile.taxi.presentation.components.InternetOffline
import uz.mobile.taxi.presentation.platform.MoviesAppScreen
import uz.mobile.taxi.presentation.util.getNavigationBarHeight

class PrivacyScreen : MoviesAppScreen {
    @Composable
    override fun Content() {
        val networkScreenModel = remember { NetworkScreenModel() }
        val connectivityStatus by networkScreenModel.connectivityStatus.collectAsState()
        when(connectivityStatus){
            ConnectivityStatus.NOT_CONNECTED -> InternetOffline(tryAgain = { networkScreenModel.refresh() })
            else -> {PrivacyPolicyScreen()}
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(
    viewModel: PrivacyViewModel = koinInject(),
    navigator: Navigator = LocalNavigator.currentOrThrow,
) {
    val getSettings by viewModel.getSettings.collectAsState(initial = PrivacyState.Loading)
    LaunchedEffect(Unit) { viewModel.onLaunch() }

    Scaffold(
        containerColor = Color(0xFF0F1111),
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.privacy_policy),
                        color = Color.White,
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.titleSmall
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                        contentDescription = "back",
                        tint = Color.White,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clickable { navigator.pop() }
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF0F1111),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->

        Box(modifier = Modifier.padding(paddingValues).verticalScroll(rememberScrollState())) {
            when (getSettings) {
                is PrivacyState.Error -> {
                    Text(
                        text = "Error loading privacy policy.",
                        color = Color.Red,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }

                PrivacyState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }

                is PrivacyState.Success -> {
                    val settings = (getSettings as PrivacyState.Success).settings.generalSettings
                    val title = settings?.privacyTitle ?: "Privacy Policy"
                    val description = settings?.privacyContent ?: "No description available."

                    Privacy(
                        title = title,
                        description = description,
                    )
                }

                else -> {
                    Text(
                        text = "Unexpected state.",
                        color = Color.Gray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun Privacy(
    title: String,
    description: String,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = title,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp, top = 16.dp),
            style = TextStyle(
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(Res.font.inter_bold, FontWeight.Normal, FontStyle.Normal)),
                fontWeight = FontWeight(700),
                color = Color.White,
            )
        )

        Text(
            color = Color.White,
            text = cleanHtmlContent(description) ,
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(Res.font.inter_light, FontWeight.Normal, FontStyle.Normal)),
                fontWeight = FontWeight(400),
                color = Color.White,
            )
        )

        Spacer(modifier = Modifier.height((75 + getNavigationBarHeight()).dp))
    }
}

fun cleanHtmlContent(htmlString: String): String {
    return htmlString
        .replace(Regex("</?p>"), "")
        .replace("<br>", "\n")
}
