package uz.mobile.joybox.presentation.screens.profile.billing.biilingScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import chaintech.network.connectivitymonitor.ConnectivityStatus
import joybox.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.core.parameter.parametersOf
import uz.mobile.joybox.domain.model.PaymentTypes
import uz.mobile.joybox.presentation.components.*
import uz.mobile.joybox.presentation.lifecycle.rememberAppSwitchObserver
import uz.mobile.joybox.presentation.platform.MoviesAppScreen
import uz.mobile.joybox.presentation.screens.profile.billing.balanceReplanishment.BalanceReplenishmentScreen
import uz.mobile.joybox.presentation.screens.profile.billing.balanceReplanishment.PaymentMethodSelection
import uz.mobile.joybox.presentation.theme.balanceErrorBackgroundColor
import uz.mobile.joybox.presentation.theme.getTypography
import uz.mobile.joybox.presentation.util.getNavigationBarHeight

class BillingScreen(private val id: Int) : MoviesAppScreen {


    @Composable
    override fun Content() {
        val viewModel: BillingScreenViewModel = koinScreenModel { parametersOf(id) }
        val navigator = LocalNavigator.currentOrThrow

        val networkStatus by viewModel.networkStatus.collectAsState()
        val paymentState by viewModel.paymentState.collectAsState()
        val subscriptionState by viewModel.subscriptionState.collectAsState()

        HandleAppLifecycle(id = id, viewModel = viewModel)

        LaunchedEffect(subscriptionState.isSubscribed, subscriptionState.hasMoved) {
            if (subscriptionState.isSubscribed && subscriptionState.hasMoved) {
                navigator.popUntilRoot()
            }
        }

        when (networkStatus) {
            ConnectivityStatus.NOT_CONNECTED -> InternetOffline(
                tryAgain = { viewModel.refreshNetwork() }
            )

            else -> BillingContent(
                viewModel = viewModel,
                navigator = navigator,
                paymentState = paymentState,
                subscriptionState = subscriptionState,
            )
        }
    }
}

@Composable
private fun HandleAppLifecycle(id: Int, viewModel: BillingScreenViewModel) {
    rememberAppSwitchObserver(
        onAppDidEnterBackground = { viewModel.handleAppBackground() },
        onAppDidBecomeActive = { viewModel.checkSubscriptionStatus(id) }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BillingContent(
    viewModel: BillingScreenViewModel,
    navigator: Navigator,
    paymentState: PaymentState,
    subscriptionState: SubscriptionState,
) {
    val focusManager = LocalFocusManager.current

    val pullRefreshState = rememberPullRefreshState(
        refreshing = paymentState.isLoading,
        onRefresh = viewModel::refreshData
    )



    Column(
        modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        MainTopBar(
            onBackButtonClicked = { navigator.pop() },
            text = stringResource(Res.string.payment_and_subscription)
        )


        Box(
            modifier = Modifier.fillMaxSize().pullRefresh(pullRefreshState)
        ) {
            PaymentContent(
                paymentTypes = paymentState.payments,
                viewModel = viewModel,
                focusManager = focusManager
            )

            PullRefreshIndicator(
                refreshing = paymentState.isLoading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }

    }

    HandleSubscriptionDialogs(subscriptionState, onBackToRoot = {
        navigator.popUntilRoot()
        viewModel.clearSubscriptionError()
    }, onCloseClicked = {
        viewModel.clearSubscriptionError()
    }, openPaymentLink = { url ->
        viewModel.openPaymentUrl(url)
    }, onNavigateReplenishment = {
        navigator.push(BalanceReplenishmentScreen())
        viewModel.clearSubscriptionError()
    })


}

@Composable
private fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.Cyan)
    }
}

@Composable
private fun PaymentContent(
    paymentTypes: List<PaymentTypes>,
    viewModel: BillingScreenViewModel,
    focusManager: FocusManager
) {
    LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
        item {
            PaymentMethodSelection(
                modifier = Modifier,
                paymentMethods = paymentTypes,
                selectedPaymentMethod = null,
                onPaymentMethodSelected = { method ->
                    focusManager.clearFocus(force = true)
                    viewModel.initiateSubscription(method.alias)
                }
            )
        }
        item {
            Spacer(modifier = Modifier.height((75 + getNavigationBarHeight()).dp))
        }
    }
}

@Composable
private fun HandleSubscriptionDialogs(
    subscriptionState: SubscriptionState,
    onBackToRoot: () -> Unit,
    onCloseClicked: () -> Unit,
    openPaymentLink: (String) -> Unit,
    onNavigateReplenishment: () -> Unit

) {
    subscriptionState.successResponse?.let { response ->
        if (response.isFree == true) {
            PromoCodeActivatedDialog(
                image = Res.drawable.icon_circle_image,
                title = response.title.orEmpty(),
                description = response.message.orEmpty(),
                onDismissRequest = onBackToRoot
            )
        } else {
            response.paymentLink?.let {
                openPaymentLink(it)
            }
        }
    }

    if (subscriptionState.showError) {
        BalanceErrorDialog(
            description = subscriptionState.errorMessage.asString(),
            onCloseClicked = onCloseClicked,
            onNavigateReplenishment = onNavigateReplenishment
        )
    }

}


@Composable
fun BalanceErrorDialog(
    description: String,
    onCloseClicked: () -> Unit,
    onNavigateReplenishment: () -> Unit
) {
    Dialog(onDismissRequest = onCloseClicked) {
        Box(
            modifier = Modifier.background(
                color = balanceErrorBackgroundColor,
                RoundedCornerShape(8.dp)
            ).padding(horizontal = 10.dp, vertical = 20.dp)
        ) {

            Icon(
                modifier = Modifier.padding(end = 20.dp).size(24.dp)
                    .clickable(onClick = onCloseClicked)
                    .align(alignment = Alignment.TopEnd),
                imageVector = Icons.Default.Close,
                contentDescription = "",
                tint = Color.White
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(width = 101.dp, height = 87.dp),
                    painter = painterResource(Res.drawable.ic_search_empty_placeholder),
                    contentDescription = "",
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = description,
                    textAlign = TextAlign.Center,
                    style = getTypography().titleLarge.copy(
                        color = Color.White,
                        lineHeight = 23.sp
                    )
                )

                Spacer(Modifier.height(12.dp))

                MainButton(text = stringResource(Res.string.balance_replanishment)) {
                    onNavigateReplenishment()
                }
            }
        }
    }
}
