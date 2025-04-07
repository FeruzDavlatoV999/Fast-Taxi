package uz.mobile.joybox.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AuthTopBar(
    onBackButtonClicked: () -> Unit,
    onLanguageChanged: (String) -> Unit,
    selectedLanguage: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        BackButton(onClick = onBackButtonClicked)

        Spacer(modifier = Modifier.weight(1f))

        LanguageDropdownBorderless(onLanguageSelected = onLanguageChanged, selectedLanguage)

    }
}
