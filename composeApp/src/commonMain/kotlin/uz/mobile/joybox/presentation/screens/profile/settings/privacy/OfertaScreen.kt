package uz.mobile.joybox.presentation.screens.profile.settings.privacy

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import chaintech.network.connectivitymonitor.ConnectivityStatus
import joybox.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import uz.mobile.joybox.presentation.NetworkScreenModel
import uz.mobile.joybox.presentation.components.InternetOffline
import uz.mobile.joybox.presentation.platform.MoviesAppScreen

class OfertaScreen : MoviesAppScreen {
    @Composable
    override fun Content() {
        val networkScreenModel = remember { NetworkScreenModel() }
        val connectivityStatus by networkScreenModel.connectivityStatus.collectAsState()
        when(connectivityStatus){
            ConnectivityStatus.NOT_CONNECTED -> InternetOffline(tryAgain = { networkScreenModel.refresh() })
            else -> {
                Oferta()
            }
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Oferta(
    viewModel: PrivacyViewModel = koinInject(),
    navigator: Navigator = LocalNavigator.currentOrThrow,
) {
    val getSettings by viewModel.getSettings.collectAsState()

    LaunchedEffect(Unit) { viewModel.onLaunch() }

    Scaffold(
        containerColor = Color(0xFF0F1111), modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(
                    text = stringResource(Res.string.user_agreement),
                    color = Color.White,
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.titleSmall
                )
            }, navigationIcon = {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                    contentDescription = "back",
                    tint = Color.White,
                    modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 0.dp).clickable {
                        navigator.pop()
                    })
            },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF0F1111),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }) {paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).verticalScroll(rememberScrollState())) {
            when (getSettings) {
                is PrivacyState.Error -> {
                    Text("Error")
                }

                PrivacyState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.Cyan)
                    }
                }

                is PrivacyState.Success -> {
                    Privacy(
                        title = (getSettings as PrivacyState.Success).settings.generalSettings?.termsTitle
                            ?: "",
                        description = (getSettings as PrivacyState.Success).settings.generalSettings?.termsContent
                            ?: "",

                        )

                }

                else -> {}
            }
        }
    }
}