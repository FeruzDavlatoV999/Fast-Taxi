package uz.mobile.joybox.presentation.screens.notifications

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import joybox.composeapp.generated.resources.Res
import joybox.composeapp.generated.resources.notifications
import org.jetbrains.compose.resources.stringResource
import uz.mobile.joybox.domain.model.Notification
import uz.mobile.joybox.presentation.components.CachedAsyncImage
import uz.mobile.joybox.presentation.components.MainTopBar
import uz.mobile.joybox.presentation.platform.MoviesAppScreen
import uz.mobile.joybox.presentation.theme.getTypography

class NotificationsDetailScreen(
    private val notification: Notification
) : MoviesAppScreen {


    @Composable
    override fun Content() {
        NotificationsDetailScreenContent()
    }


    @Composable
    fun NotificationsDetailScreenContent(
        navigator: Navigator = LocalNavigator.currentOrThrow
    ) {

        Column(
            modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing)
        ) {

            MainTopBar(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.notifications)
            ) {
                navigator.pop()
            }
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                CachedAsyncImage(
                    url = notification.imgUrl,
                    modifier = Modifier.fillMaxWidth().height(222.dp),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    text = notification.title,
                    style = getTypography().labelMedium.copy(
                        color = Color.White,
                        lineHeight = 25.sp,
                        fontSize = 21.sp
                    ),
                    color = Color.White,
                )

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    text = notification.toStringDate(),
                    overflow = TextOverflow.Ellipsis,
                    style = getTypography().displaySmall,
                )

                Spacer(modifier = Modifier.height(15.dp))


                Text(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    text = notification.description,
                    overflow = TextOverflow.Ellipsis,
                    style = getTypography().displayMedium.copy(lineHeight = 21.sp),
                )


            }
        }
    }


}