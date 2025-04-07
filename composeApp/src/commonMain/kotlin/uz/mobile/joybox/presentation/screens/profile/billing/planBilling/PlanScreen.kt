package uz.mobile.joybox.presentation.screens.profile.billing.planBilling

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.LocalDateTime
import joybox.composeapp.generated.resources.*
import network.chaintech.kmp_date_time_picker.utils.capitalize
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.stringResource
import uz.mobile.joybox.data.remote.dto.SubscriptionsResponse
import uz.mobile.joybox.domain.model.Status
import uz.mobile.joybox.domain.model.processSubscriptionsByStatus
import uz.mobile.joybox.presentation.screens.profile.billing.EmptyBillingScreen
import uz.mobile.joybox.presentation.util.getNavigationBarHeight

@Composable
fun PlanScreen(
    viewModel: PlanScreenViewModel,
) {

    val planned by viewModel.getMySubscription.collectAsState()
    val plans = if (planned is PlanScreenViewModel.PlanState.SuccessMySubscription) (planned as PlanScreenViewModel.PlanState.SuccessMySubscription).mySubscription else null
    val statusesToFilter =
        listOf(Status.ACTIVE, Status.INACTIVE, Status.EXPIRED, Status.PENDING_FOR_PAYMENT)


    if (planned is PlanScreenViewModel.PlanState.Loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.Cyan)
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0F0F0F))
                .padding(16.dp)
        ) {

            if(plans?.subscriptions?.isEmpty() == true){
                EmptyBillingScreen()
            }else{
                LazyColumn {
                    item {
                        SubscriptionsList(plans?.subscriptions, statusesToFilter)
                    }
                    item {
                        Spacer(modifier = Modifier.height((75 + getNavigationBarHeight()).dp))
                    }
                }
            }

        }
    }
}

@Composable
fun SubscriptionsList(
    subscriptions: List<SubscriptionsResponse.Subscription?>?,
    statusesToExclude: List<Status>,
) {
    val groupedSubscriptions = remember(subscriptions, statusesToExclude) {
        processSubscriptionsByStatus(subscriptions)
    }
    groupedSubscriptions.forEach { (status, subscriptions) ->

        StatusHeader(status = status)

        subscriptions.forEach { item ->
            val createAt =
                if (item?.createdAt != null) "${stringResource(Res.string.from)}: ${
                    formatDate(
                        item.createdAt
                    )
                }" else ""
            val expiredAt =
                if (item?.expiredAt != null) " ${stringResource(Res.string.by)}: ${
                    formatDate(
                        item.expiredAt
                    )
                }" else ""
            PlanItem(
                title = item?.term?.name ?: "",

                date = "$createAt $expiredAt",
                status = when (status) {
                    Status.ACTIVE -> item?.status?.name.orEmpty()
                    Status.EXPIRED -> item?.status?.name.orEmpty()
                    Status.INACTIVE -> item?.status?.name.orEmpty()
                    Status.PENDING_FOR_PAYMENT -> item?.status?.name.orEmpty()
                },
                statusColor = when (status) {
                    Status.ACTIVE -> Color(0xFF58C93E)
                    Status.EXPIRED -> Color(0xFFFA4932)
                    Status.INACTIVE -> Color(0xFFFA4932)
                    Status.PENDING_FOR_PAYMENT -> Color(0xFF2196F3)
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@Composable
fun PlanItem(title: String, date: String, status: String, statusColor: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1C1C1E), RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = date,
                    color = Color(0xFFB3B3B3),
                    fontSize = 14.sp
                )
            }
            StatusChip(status = status, backgroundColor = statusColor)
        }
    }
}

@Composable
fun StatusHeader(status: Status) {
    Text(
        text = when (status) {
            Status.ACTIVE -> stringResource(Res.string.current_plan)
            Status.INACTIVE -> stringResource(Res.string.payment_history)
            Status.EXPIRED -> stringResource(Res.string.expired_subscriptions)
            Status.PENDING_FOR_PAYMENT -> stringResource(Res.string.pending_payments)
        },
        style = MaterialTheme.typography.h6,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    )
}


@Composable
fun StatusChip(status: String, backgroundColor: Color) {
    Box(
        modifier = Modifier
            .background(backgroundColor, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
            text = status.capitalize(),
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = FontFamily(
                    Font(
                        Res.font.inter_medium,
                        FontWeight.Normal,
                        FontStyle.Normal
                    )
                )
            ),
            fontWeight = FontWeight(500),
            color = Color(0xFFEEEFF0),
            textAlign = TextAlign.Center,
        )

    }
}


fun formatDate(dateString: String): String {
    val adjustedDateString = dateString.replace(" ", "T")
    val dateTime = LocalDateTime.parse(adjustedDateString)
    return dateTime.date.toString()
}