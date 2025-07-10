package uz.mobile.taxi.presentation.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import chaintech.network.connectivitymonitor.ConnectivityStatus
import taxi.composeapp.generated.resources.*
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import uz.mobile.taxi.presentation.AppScreenModel
import uz.mobile.taxi.presentation.NetworkScreenModel
import uz.mobile.taxi.presentation.components.CustomLogOutDialog
import uz.mobile.taxi.presentation.components.InternetOffline
import uz.mobile.taxi.presentation.components.ProfileSections
import uz.mobile.taxi.presentation.components.ReviewDialog
import uz.mobile.taxi.presentation.platform.MoviesAppScreen
import uz.mobile.taxi.presentation.screens.naviagationActions.navigateToOfertaScreen
import uz.mobile.taxi.presentation.screens.naviagationActions.navigateToPaymentScreen
import uz.mobile.taxi.presentation.screens.naviagationActions.navigateToPrivacyScreen
import uz.mobile.taxi.presentation.screens.naviagationActions.navigateToSettingsItemsScreen
import uz.mobile.taxi.presentation.screens.naviagationActions.navigateToUserUpdateScreen
import uz.mobile.taxi.presentation.screens.profile.userComponent.ExpandableProfileHeader
import uz.mobile.taxi.presentation.screens.profile.userComponent.LogoutSection
import uz.mobile.taxi.presentation.screens.profile.userComponent.SocialMediaIconsRow
import uz.mobile.taxi.presentation.util.getNavigationBarHeight


class ProfileScreen : MoviesAppScreen {

    @Composable
    override fun Content() {
        val networkScreenModel: NetworkScreenModel = koinInject()
        val connectivityStatus by networkScreenModel.connectivityStatus.collectAsState()
        when (connectivityStatus) {
            ConnectivityStatus.NOT_CONNECTED -> InternetOffline(tryAgain = { networkScreenModel.refresh() })
            else -> {
                ProfileCheckContent()
            }
        }
    }

    @Composable
    fun ProfileCheckContent(
        viewModel: ProfileViewModel = koinScreenModel(),
        appViewModel: AppScreenModel = koinInject(),
        navigator: Navigator = LocalNavigator.currentOrThrow,
    ) {
        val user by viewModel.getUser.collectAsState()
        val getSettings by viewModel.getSettings.collectAsState()

        LaunchedEffect(Unit) { viewModel.onLaunch() }

        var showReviewDialog by remember { mutableStateOf(false) }
        var showLogOutDialog by remember { mutableStateOf(false) }
        var ratings by remember { mutableStateOf(0f) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .background(Color(0xFF121212))
        ) {
            LazyColumn {
                item {
                    ExpandableProfileHeader(
                        name = if (user is UserState.Success) (user as UserState.Success).user.firstname.orEmpty() else "",
                        email = if (user is UserState.Success) (user as UserState.Success).user.phone.orEmpty() else "",
                        id = if (user is UserState.Success) (user as UserState.Success).user.userId.orEmpty() else "",
                        onClick = {
                            navigateToUserUpdateScreen(navigator = navigator)
                        },
                        url = if (user is UserState.Success) (user as UserState.Success).user.avatar.orEmpty() else ""
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    ProfileSections(
                        title = stringResource(Res.string.settings),
                        isIconVisible = true,
                        onClick = {
                            navigateToSettingsItemsScreen(navigator = navigator)
                        })
                }
                item {
                    ProfileSections(
                        title = stringResource(Res.string.payment_subscription),
                        isIconVisible = true,
                        onClick = {
                            navigateToPaymentScreen(navigator)
                        },
                    )
                }
                item {
                    ProfileSections(
                        title = stringResource(Res.string.privacy_policy),
                        isIconVisible = true,
                        onClick = {
                            navigateToPrivacyScreen(navigator)
                        })
                }
                item {
                    ProfileSections(
                        title = stringResource(Res.string.user_agreement),
                        isIconVisible = true,
                        onClick = {
                            navigateToOfertaScreen(navigator)
                        })
                }
                item {
                    ProfileSections(
                        title = stringResource(Res.string.share),
                        isIconVisible = true,
                        onClick = {
                            viewModel.shareData()
                        })
                }
                item {
                    ProfileSections(
                        title = stringResource(Res.string.rate_app),
                        isIconVisible = true,
                        onClick = {
                            showReviewDialog = true
                        })
                }
                item {
                    ProfileSections(
                        title = stringResource(Res.string.support),
                        isIconVisible = true,
                        onClick = {
                            viewModel.getUrl(
                                if (getSettings is UserState.SuccessSettings) (getSettings as UserState.SuccessSettings).settings.generalSettings?.telegram
                                    ?: "" else ""
                            )
                        })
                }

                item {
                    SocialMediaIconsRow(
                        onClick = { launch ->
                            viewModel.getUrl(launch)
                        },
                        socialLink = if (getSettings is UserState.SuccessSettings) (getSettings as UserState.SuccessSettings).settings.generalSettings else null
                    )


                    Spacer(modifier = Modifier.height(24.dp))
                    LogoutSection(onClick = {
                        showLogOutDialog = true
                    })
                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "Version: 1.0",
                        modifier = Modifier.align(Alignment.Center).padding(start = 16.dp),
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(Res.font.inter_medium, FontWeight.Normal, FontStyle.Normal)),
                            fontWeight = FontWeight(500),
                            color = Color(0xFFB9BFC1)
                        )
                    )
                    Spacer(modifier = Modifier.height((75 + getNavigationBarHeight()).dp))
                }
            }

            if (showReviewDialog) {
                ReviewDialog(
                    onDismiss = { showReviewDialog = false },
                    onSubmitRating = { rating ->
                        ratings = rating
                        showReviewDialog = false
                    },
                    selectedRating = ratings
                )
            }

            if (showLogOutDialog) {
                CustomLogOutDialog(
                    onConfirm = {
                        showLogOutDialog = false
                        appViewModel.logout()
                    },
                    onDismiss = { showLogOutDialog = false }
                )
            }
        }
    }

}






