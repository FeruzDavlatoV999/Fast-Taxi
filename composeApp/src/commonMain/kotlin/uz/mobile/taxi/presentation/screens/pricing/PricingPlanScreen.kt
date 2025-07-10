package uz.mobile.taxi.presentation.screens.pricing

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import taxi.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import uz.mobile.taxi.domain.model.Subscription
import uz.mobile.taxi.presentation.AppScreenModel
import uz.mobile.taxi.presentation.components.MainButton
import uz.mobile.taxi.presentation.components.PricingTag
import uz.mobile.taxi.presentation.components.sum
import uz.mobile.taxi.presentation.platform.MoviesAppScreen
import uz.mobile.taxi.presentation.screens.naviagationActions.navigateToBillingScreen
import uz.mobile.taxi.presentation.theme.disableButtonColor
import uz.mobile.taxi.presentation.theme.getTypography
import uz.mobile.taxi.presentation.theme.headlineMediumTextColorDark
import uz.mobile.taxi.presentation.theme.mainButtonTextColorDark


class PricingPlanScreen(private val fromDetails: Boolean = false) : MoviesAppScreen {


    @Composable
    override fun Content() {
        PricingScreenContent()
    }


    @Composable
    fun PricingScreenContent(
        viewModel: PricingPlanScreenModel = koinScreenModel(),
        navigator: Navigator = LocalNavigator.currentOrThrow,
        appModel: AppScreenModel = koinInject()
    ) {

        val plans = viewModel.plans
        val selectedPlan = viewModel.selectedPlan

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.choose_plan),
                style = getTypography().headlineSmall,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.fillMaxWidth().height(12.dp))

            Text(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                text = stringResource(Res.string.choose_plan_description),
                style = getTypography().bodyLarge,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.fillMaxWidth().height(10.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth().height(56.dp),
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(plans.size) { index ->
                    val plan: Subscription = plans[index]
                    PricingTag(
                        id = plan.id,
                        text = plan.name,
                        isSelected = plan.id == selectedPlan?.id,
                        onSelected = { mId -> viewModel.selectedId(id = mId) }
                    )
                }
            }

            Spacer(Modifier.fillMaxWidth().height(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(mainButtonTextColorDark),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    modifier = Modifier.height(150.dp).fillMaxWidth(),
                    painter = painterResource(Res.drawable.img_header),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                )

                Spacer(modifier = Modifier.fillMaxWidth().height(20.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = selectedPlan?.name.orEmpty(),
                        style = getTypography().headlineLarge,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Text(
                        text = selectedPlan?.price.sum(),
                        style = getTypography().headlineSmall,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.fillMaxWidth().height(24.dp))
                Spacer(
                    modifier = Modifier.fillMaxWidth().height(1.dp).padding(horizontal = 20.dp)
                        .background(color = disableButtonColor)
                )
                Spacer(modifier = Modifier.fillMaxWidth().height(24.dp))

                MainButton(
                    text = stringResource(Res.string.choose_plan),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    enableButtonColor = headlineMediumTextColorDark
                ) {
                    selectedPlan?.id?.let { planId ->
                        navigateToBillingScreen(navigator, planId)
                    }
                }

                Spacer(modifier = Modifier.fillMaxWidth().height(32.dp))


            }

            Spacer(modifier = Modifier.fillMaxWidth().height(10.dp))


            Text(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 20.dp).clickable {
                    if (fromDetails) navigator.pop()
                    else appModel.navigateHome()
                },
                text = stringResource(Res.string.choose_plan_later),
                style = getTypography().titleMedium,
                textAlign = TextAlign.Center
            )

            if (fromDetails) Spacer(modifier = Modifier.height(40.dp))

        }
    }


}