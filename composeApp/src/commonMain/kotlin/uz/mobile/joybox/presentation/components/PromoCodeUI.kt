package uz.mobile.joybox.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import joybox.composeapp.generated.resources.Res
import joybox.composeapp.generated.resources.apply_promo_code
import joybox.composeapp.generated.resources.enter_promo_code
import joybox.composeapp.generated.resources.payment
import org.jetbrains.compose.resources.stringResource
import uz.mobile.joybox.domain.util.UniversalText
import uz.mobile.joybox.presentation.screens.profile.billing.balanceReplanishment.BalanceReplenishmentViewModel
import uz.mobile.joybox.presentation.theme.getTypography
import uz.mobile.joybox.presentation.theme.hintTextColorDark

@Composable
fun PromoCodeUI(
    viewModel: BalanceReplenishmentViewModel,
    onClickPromoCode: () -> Unit,
    onClickPayment: () -> Unit,
    modifier: Modifier,
    isLoading:Boolean = false,
    error: UniversalText = UniversalText.Empty,
) {
    Column(
        modifier = modifier
            .fillMaxWidth().padding(top = 18.dp).safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        MainWhiteButton(text = stringResource(Res.string.payment)) {
            onClickPayment()
        }

        Spacer(modifier = Modifier.height(16.dp))

        InputTextField(
            modifier = Modifier,
            value = viewModel.promoCode,
            onValueChange = viewModel::updatePromoCode,
            placeholder = {
                Text(
                    text = stringResource(Res.string.enter_promo_code),
                    style = getTypography().bodyMedium.copy(color = hintTextColorDark)
                )
            },
            error = error
        )

        Spacer(modifier = Modifier.height(16.dp))

        MainButton(
            text = stringResource(Res.string.apply_promo_code),
            isLoading = isLoading
        ) {
            onClickPromoCode()
        }
    }
}