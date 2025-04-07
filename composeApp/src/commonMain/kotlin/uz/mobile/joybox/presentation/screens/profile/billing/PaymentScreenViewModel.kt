package uz.mobile.joybox.presentation.screens.profile.billing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel

class PaymentScreenViewModel : ScreenModel {
    var selectedTab: Int by mutableStateOf(0)
        private set

    fun updateTab(tab: Int) {
        selectedTab = tab
    }
}
