package uz.mobile.joybox.presentation.screens.profile.userComponent

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import joybox.composeapp.generated.resources.*
import uz.mobile.joybox.data.remote.dto.GeneralSettings
import uz.mobile.joybox.presentation.components.SocialMediaIcon

@Composable
fun SocialMediaIconsRow(
    socialLink: GeneralSettings.GeneralSetting?,
    onClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 38.dp, start = 6.dp)
    ) {
        SocialMediaIcon(
            iconResId = Res.drawable.ic_instagram,
            contentDescription = "Instagram",
            onClick = {
                onClick("${socialLink?.instagram}")
            }
        )

        SocialMediaIcon(
            iconResId = Res.drawable.facebook,
            contentDescription = "Facebook",
            onClick = {
                onClick("${socialLink?.facebook}")
            }
        )

        SocialMediaIcon(
            iconResId = Res.drawable.telegram,
            contentDescription = "Telegram",
            onClick = { onClick("${socialLink?.telegram}") }
        )

        SocialMediaIcon(
            iconResId = Res.drawable.youtube,
            contentDescription = "YouTube",
            onClick = { onClick("${socialLink?.youtobe}")}
        )
    }
}