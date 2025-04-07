package uz.mobile.joybox.presentation.screens.notifications

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import joybox.composeapp.generated.resources.Res
import joybox.composeapp.generated.resources.ic_notification_placeholder
import joybox.composeapp.generated.resources.nothing_found
import joybox.composeapp.generated.resources.notifications
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import uz.mobile.joybox.domain.model.Notification
import uz.mobile.joybox.presentation.components.CachedAsyncImage
import uz.mobile.joybox.presentation.components.MainTopBar
import uz.mobile.joybox.presentation.platform.MoviesAppScreen
import uz.mobile.joybox.presentation.theme.getTypography
import uz.mobile.joybox.presentation.theme.headlineMediumTextColorDark
import uz.mobile.joybox.presentation.util.getNavigationBarHeight


class NotificationsScreen : MoviesAppScreen {


    @Composable
    override fun Content() {
        NotificationsScreenContent()
    }

    @Composable
    fun NotificationsScreenContent(
        viewModel: NotificationsScreenModel = koinScreenModel(),
        navigator: Navigator = LocalNavigator.currentOrThrow
    ) {

        val notifications = viewModel.notificationsFlow.collectAsLazyPagingItems()

        Column(
            modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            MainTopBar(text = stringResource(Res.string.notifications)) {
                navigator.pop()
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (notifications.itemCount == 0) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(Res.drawable.ic_notification_placeholder),
                        contentDescription = ""
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = stringResource(Res.string.nothing_found),
                        style = getTypography().bodyMedium.copy(color = headlineMediumTextColorDark)
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(
                        start = 14.dp,
                        end = 8.dp,
                        top = 10.dp,
                        bottom = 10.dp
                    )
                ) {
                    items(notifications.itemCount) { index ->
                        val data: Notification? = notifications[index]
                        data?.let {
                            NotificationCard(
                                notification = data,
                                date = getDate(notifications, index),
                            ){
                                navigator.push(NotificationsDetailScreen(it))
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height((75 + getNavigationBarHeight()).dp))
                    }
                }
            }
        }
    }

    private fun getDate(notifications: LazyPagingItems<Notification>, index: Int): String {
        return if (index == 0 || notifications[index]?.date != notifications[index - 1]?.date) notifications[index]?.date.orEmpty()
        else ""
    }


    @Composable
    fun NotificationCard(
        notification: Notification,
        date: String,
        modifier: Modifier = Modifier,
        onClick: (Notification) -> Unit
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.Transparent, RoundedCornerShape(10.dp))
                .clickable(onClick = { onClick(notification) })
        ) {


            if (date.isNotBlank()) {
                Text(
                    text = date,
                    modifier = Modifier.padding(top = 20.dp),
                    style = getTypography().titleMedium.copy(
                        color = Color.White,
                        lineHeight = 20.sp
                    )
                )
            }

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                CachedAsyncImage(
                    url = notification.imgUrl,
                    modifier = Modifier
                        .width(120.dp).height(92.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))


                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = notification.title,
                        style = getTypography().labelMedium.copy(color = Color.White),
                        color = Color.White,
                        maxLines = 2
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = notification.description,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis,
                        style = getTypography().displaySmall,
                    )


                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = notification.toStringDate(),
                        style = getTypography().displaySmall,
                        maxLines = 1
                    )
                }
            }
        }
    }

}