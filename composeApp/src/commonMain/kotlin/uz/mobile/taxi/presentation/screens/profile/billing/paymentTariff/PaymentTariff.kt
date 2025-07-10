package uz.mobile.taxi.presentation.screens.profile.billing.paymentTariff

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import taxi.composeapp.generated.resources.*
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.stringResource
import uz.mobile.taxi.presentation.components.MainButton
import uz.mobile.taxi.presentation.screens.naviagationActions.navigateToBillingScreen
import uz.mobile.taxi.presentation.theme.disableButtonColor
import uz.mobile.taxi.presentation.theme.getTypography
import uz.mobile.taxi.presentation.theme.headlineMediumTextColorDark
import uz.mobile.taxi.presentation.theme.mainButtonTextColorDark
import uz.mobile.taxi.presentation.util.getNavigationBarHeight

@Composable
fun PaymentTariff(
    navigator: Navigator = LocalNavigator.currentOrThrow,
    viewModel: PaymentTariffViewModel,
) {
    val subSubscriptions by viewModel.getSubscription.collectAsState()
    val getBalance by viewModel.getBalance.collectAsState()

    if (getBalance is PaymentTariffViewModel.PaymentTariffState.Loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.Cyan)
        }
    } else {
        LazyColumn(modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)) {
            val getBalances =
                if (getBalance is PaymentTariffViewModel.PaymentTariffState.SuccessBalance) (getBalance as PaymentTariffViewModel.PaymentTariffState.SuccessBalance).payment else null
            val subscriptions =
                if (subSubscriptions is PaymentTariffViewModel.PaymentTariffState.SuccessSubscription) (subSubscriptions as PaymentTariffViewModel.PaymentTariffState.SuccessSubscription).mySubscription else null

            item {
                getBalances?.balance?.amount?.let { balance ->
                    BalanceCard(balance.toString() + " " + getBalances.balance.currency)
                }
            }

            items(subscriptions?.subscriptions?.size ?: 0) { index ->
                val types = subscriptions?.subscriptions?.get(index)
                SubscriptionCard(
                    duration = types?.term?.name ?: "",
                    price = "${types?.currency?.pivot?.price} ${types?.currency?.name}",
                    originalPrice = if (types?.currency?.pivot?.oldPrice != null) "${types.currency.pivot.oldPrice} ${types.currency.name}" else null,
                    bestPriceTag = types?.isBestseller ?: false,
                    onBuyClick = {
                        navigateToBillingScreen(navigator, types?.id ?: 0)
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height((75 + getNavigationBarHeight()).dp))
            }
        }
    }
}


@Composable
fun BalanceCard(balance: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 12.dp)
            .background(Color(0xFF1A1A1A), RoundedCornerShape(8.dp)),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(start = 20.dp, top = 28.dp, bottom = 8.dp, end = 16.dp),
            text = stringResource(Res.string.balance),
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(
                    Font(
                        Res.font.inter_bold,
                        FontWeight.Normal,
                        FontStyle.Normal
                    )
                ),
                fontWeight = FontWeight(600),
                color = Color(0xFFEEEFF0),
                textAlign = TextAlign.Center,
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.padding(start = 20.dp, bottom = 20.dp, end = 16.dp),
            text = formatNumberWithBalance(balance),
            style = TextStyle(
                fontSize = 20.sp,
                fontFamily = FontFamily(
                    Font(
                        Res.font.inter_bold,
                        FontWeight.Normal,
                        FontStyle.Normal
                    )
                ),
                fontWeight = FontWeight(700),
                color = Color(0xFFEEEFF0),
            )
        )
    }
}

fun formatNumberWithBalance(input: String): String {
    val parts = input.trim().split(" ")
    val numberPart = parts[0]
    val currency = if (parts.size > 1) parts[1] else ""

    val numberComponents = numberPart.split(".")
    val integerPart = numberComponents[0]
    val fractionalPart = if (numberComponents.size > 1) numberComponents[1] else null

    val formattedIntegerPart = integerPart.reversed().chunked(3).joinToString(" ").reversed()

    return if (!fractionalPart.isNullOrEmpty()) {
        "$formattedIntegerPart.${fractionalPart.padEnd(2, '0')} $currency"
    } else {
        "$formattedIntegerPart $currency"
    }
}


@Composable
fun SubscriptionCard(
    duration: String,
    price: String,
    originalPrice: String? = null,
    bestPriceTag: Boolean = false,
    onBuyClick: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .background(mainButtonTextColorDark, RoundedCornerShape(8.dp))
            .padding(vertical = 20.dp, horizontal = 20.dp)

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(vertical = 10.dp),
                text = duration,
                style = getTypography().bodySmall.copy(
                    color = headlineMediumTextColorDark,
                    fontSize = 15.sp
                )
            )
            if (bestPriceTag) {

                Box(
                    modifier = Modifier
                        .background(
                            color = Color(0xFF2CC821),
                            shape = RoundedCornerShape(50)
                        ).padding(horizontal = 12.dp)
                ) {
                    Text(
                        fontSize = 12.sp,
                        text = stringResource(Res.string.best_price),
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center).padding(vertical = 7.dp),
                        style = TextStyle(
                            fontFamily = FontFamily(
                                Font(
                                    Res.font.inter_medium,
                                    FontWeight.Normal,
                                    FontStyle.Normal
                                )
                            ),
                            fontWeight = FontWeight(500)
                        )
                    )
                }
            }
        }

        if (originalPrice != null) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = formatNumberWithBalance(originalPrice),
                style = getTypography().bodyMedium.copy(
                    color = headlineMediumTextColorDark,
                    textDecoration = TextDecoration.LineThrough
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = formatNumberWithBalance(price),
            style = getTypography().headlineLarge.copy(fontWeight = FontWeight(700))
        )

        Spacer(modifier = Modifier.height(8.dp))

        Divider(modifier = Modifier.height(1.dp).fillMaxWidth(), color = disableButtonColor)

        Spacer(modifier = Modifier.height(8.dp))

        MainButton(
            stringResource(Res.string.buy),
            onClicked = onBuyClick,
            modifier = Modifier.height(44.dp),
            textStyle = getTypography().labelMedium.copy(fontSize = 14.sp, lineHeight = 17.sp)
        )
    }
}