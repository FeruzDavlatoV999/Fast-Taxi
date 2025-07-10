package uz.mobile.taxi.presentation.screens.profile.billing

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import taxi.composeapp.generated.resources.*
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.stringResource
import uz.mobile.taxi.presentation.components.MainTopBar
import uz.mobile.taxi.presentation.platform.MoviesAppScreen
import uz.mobile.taxi.presentation.screens.profile.billing.balanceReplanishment.BalanceReplenishment
import uz.mobile.taxi.presentation.screens.profile.billing.balanceReplanishment.BalanceReplenishmentViewModel
import uz.mobile.taxi.presentation.screens.profile.billing.paymentTariff.PaymentTariff
import uz.mobile.taxi.presentation.screens.profile.billing.paymentTariff.PaymentTariffViewModel
import uz.mobile.taxi.presentation.screens.profile.billing.planBilling.PlanScreen
import uz.mobile.taxi.presentation.screens.profile.billing.planBilling.PlanScreenViewModel

class PaymentScreen : MoviesAppScreen {


    @Composable
    override fun Content() {
        Payment()
    }

    @Composable
    fun Payment(
        navigator: Navigator = LocalNavigator.currentOrThrow,
        screenModel: PaymentScreenViewModel = koinScreenModel(),
        screenModel2: BalanceReplenishmentViewModel = koinScreenModel(),
        screenModel4: PaymentTariffViewModel = koinScreenModel(),
        screenModel5: PlanScreenViewModel = koinScreenModel(),
    ){
        Column(
            modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ){
            MainTopBar(onBackButtonClicked = {
                navigator.pop()
            }, text = stringResource(Res.string.payment_and_subscription))


            HorizontalTabsWithIndex(screenModel.selectedTab, onTabSelected = { index ->
                screenModel.updateTab(index)
            })

            when(screenModel.selectedTab){
                0 ->{
                    PaymentTariff(navigator,screenModel4)
                }
                1->{
                    BalanceReplenishment(screenModel2)
                }
                2->{
                    PlanScreen(screenModel5)
                }
            }
        }
    }
}

@Composable
fun HorizontalTabsWithIndex(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
) {
    val tabTitles = listOf(stringResource(Res.string.subscriptions), stringResource(Res.string.balance_replanishment),stringResource(Res.string.current_plan))

    Box(
        modifier = Modifier.padding(vertical = 16.dp)
            .fillMaxWidth()
            .background(Color(0xFF0F1111))
    ) {
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            edgePadding = 16.dp,
            containerColor = Color.Transparent,
            indicator = { },
            divider = { }
        ) {
            tabTitles.forEachIndexed { index, title ->
                val isSelected = selectedTabIndex == index
                val backgroundColor = if (isSelected) Color.White else Color.Transparent
                val textColor = if (isSelected) Color.Black else Color.White

                Box(
                    modifier = Modifier
                        .background(
                            color = backgroundColor,
                            shape = RoundedCornerShape(50)
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onTabSelected(index)
                        }.padding(horizontal = 16.dp)
                ) {
                    Text(
                        fontSize = 14.sp,
                        text = title,
                        color = textColor,
                        modifier = Modifier.align(Alignment.Center).padding(vertical = 8.dp),
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
    }
}

