package uz.mobile.taxi.presentation.screens.profile.billing.balanceReplanishment


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import taxi.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import uz.mobile.taxi.domain.model.PaymentTypes
import uz.mobile.taxi.domain.util.ThousandsSeparatorVisualTransformation
import uz.mobile.taxi.domain.util.UniversalText
import uz.mobile.taxi.presentation.components.BlankTextField
import uz.mobile.taxi.presentation.components.CachedAsyncImage
import uz.mobile.taxi.presentation.components.MainTopBar
import uz.mobile.taxi.presentation.components.PromoCodeActivatedDialog
import uz.mobile.taxi.presentation.components.PromoCodeUI
import uz.mobile.taxi.presentation.platform.MoviesAppScreen
import uz.mobile.taxi.presentation.screens.profile.billing.paymentTariff.formatNumberWithBalance
import uz.mobile.taxi.presentation.theme.disableButtonColor
import uz.mobile.taxi.presentation.theme.errorDark
import uz.mobile.taxi.presentation.theme.getTypography
import uz.mobile.taxi.presentation.theme.headlineMediumTextColorDark
import uz.mobile.taxi.presentation.util.getNavigationBarHeight


class BalanceReplenishmentScreen : MoviesAppScreen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        Column(
            modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            MainTopBar(text = stringResource(Res.string.balance_replanishment)) {
                navigator.pop()
            }

            Spacer(Modifier.height(12.dp))

            BalanceReplenishment(
                viewModel = koinScreenModel<BalanceReplenishmentViewModel>()
            )
        }
    }

}


@Composable
fun BalanceReplenishment(
    viewModel: BalanceReplenishmentViewModel,
) {
    val paymentTypes by viewModel.getPaymentTypes.collectAsState()
    val paymentBalance by viewModel.payBalance.collectAsState()
    val getPromoCode by viewModel.getPromoCode.collectAsState()

    val focusManager = LocalFocusManager.current

    val types =
        if (paymentTypes is BalanceReplenishmentViewModel.BalanceReplenishmentState.Success) (paymentTypes as BalanceReplenishmentViewModel.BalanceReplenishmentState.Success).payment else null

    val payBalance =
        if (paymentBalance is BalanceReplenishmentViewModel.BalanceReplenishmentState.SuccessPayBalance) (paymentBalance as BalanceReplenishmentViewModel.BalanceReplenishmentState.SuccessPayBalance).payBalance else null

    val promoCode =
        if (getPromoCode is BalanceReplenishmentViewModel.BalanceReplenishmentState.SuccessPromoCode) (getPromoCode as BalanceReplenishmentViewModel.BalanceReplenishmentState.SuccessPromoCode).promoCode else null


    if (paymentTypes is BalanceReplenishmentViewModel.BalanceReplenishmentState.Loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.Cyan)
        }
    } else {
        Scaffold(
            backgroundColor = Color(0xFF0F1111),
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    AmountField(viewModel)

                    types?.let {
                        PaymentScreenWithScrollableMethods(
                            modifier = Modifier,
                            viewModel,
                            onClick = {
                                viewModel.updatePayment(it)
                                focusManager.clearFocus(force = true)
                            },
                            it
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    PromoCodeUI(
                        viewModel = viewModel,
                        onClickPromoCode = {
                            viewModel.getPromoCode(viewModel.promoCode)
                        },
                        onClickPayment = {
                            viewModel.onPayClicked()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        error = viewModel.showErrorMessage,
                        isLoading = getPromoCode is BalanceReplenishmentViewModel.BalanceReplenishmentState.Loading
                    )
                    Spacer(modifier = Modifier.height((75 + getNavigationBarHeight()).dp))
                }


            }
        }

        if (viewModel.checkPromoCode) {
            PromoCodeActivatedDialog(
                Res.drawable.icon_circle_image,
                promoCode?.promoCode?.title.orEmpty(),
                promoCode?.promoCode?.message.orEmpty(),
                onDismissRequest = {
                    viewModel.updatePromoCode(false)
                }
            )
            viewModel.updatePromoCode("")
        }


        if (viewModel.payment) {
            viewModel.updatePaymentBool(false)
            payBalance?.link?.let {
                viewModel.updateBalance("")
                viewModel.getUrl(it)
            }
        }

    }
}

@Composable
fun AmountField(
    viewModel: BalanceReplenishmentViewModel,
) {

    Column(
        modifier = Modifier.fillMaxWidth()
            .background(Color(0xFF191B1C), shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
            text = stringResource(Res.string.replenishment_amount),
            style = getTypography().labelMedium.copy(
                fontWeight = FontWeight(600),
                color = headlineMediumTextColorDark
            )
        )

        BlankTextField(
            value = viewModel.amount,
            onValueChange = viewModel::onValueChanged,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            visualTransformation = ThousandsSeparatorVisualTransformation(stringResource(Res.string.sum)),
            textStyle = getTypography().headlineLarge.copy(fontWeight = FontWeight(700))
        )

        Divider(modifier = Modifier.height(1.dp), color = disableButtonColor)

        if (viewModel.showErrorBalanceMessage != UniversalText.Empty) {

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = viewModel.showErrorBalanceMessage.asString(),
                style = getTypography().bodyMedium.copy(lineHeight = 21.sp, color = errorDark)
            )
        }
    }
}


@Composable
fun PaymentScreenWithScrollableMethods(
    modifier: Modifier,
    viewModel: BalanceReplenishmentViewModel,
    onClick: (String) -> Unit,
    paymentMethod: List<PaymentTypes>,
) {
    var selectedPaymentMethod by remember { mutableStateOf(paymentMethod[0]) }

    LaunchedEffect(selectedPaymentMethod) {
        viewModel.updatePayment(selectedPaymentMethod.alias)
    }

    PaymentMethodSelection(
        modifier,
        paymentMethods = paymentMethod,
        selectedPaymentMethod = selectedPaymentMethod,
        onPaymentMethodSelected = {
            selectedPaymentMethod = it
            onClick(it.name)
        }
    )
}


@Composable
fun PaymentMethodSelection(
    modifier: Modifier,
    paymentMethods: List<PaymentTypes>,
    selectedPaymentMethod: PaymentTypes?,
    onPaymentMethodSelected: (PaymentTypes) -> Unit,
) {
    Column(
        modifier = modifier
            .padding(top = 20.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(Res.string.select_method),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column {
            paymentMethods.forEach { method ->
                PaymentMethodItem(
                    method,
                    isSelected = selectedPaymentMethod == method,
                    onClick = { onPaymentMethodSelected(method) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}


@Composable
fun PaymentMethodItem(paymentMethod: PaymentTypes, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) Color.Gray else Color(0xFF191B1C))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                if (paymentMethod.providerType == "api") {
                    CachedAsyncImage(
                        url = paymentMethod.logo,
                        modifier = Modifier.width(55.dp),
                        contentScale = ContentScale.FillWidth
                    )
                } else {
                    CachedAsyncImage(
                        url = paymentMethod.logo,
                        modifier = Modifier.width(75.dp),
                        contentScale = ContentScale.FillWidth
                    )
                }

                val sum = stringResource(Res.string.sum)
                val balance = stringResource(Res.string.balance)
                if (paymentMethod.amount != null) {
                    Text(
                        text = "$balance: ${formatNumberWithBalance("${paymentMethod.amount} $sum")}",
                        color = Color.White,
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}







