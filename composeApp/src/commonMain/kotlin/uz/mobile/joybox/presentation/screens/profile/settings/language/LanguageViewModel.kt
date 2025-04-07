package uz.mobile.joybox.presentation.screens.profile.settings.language

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import uz.mobile.joybox.datastore.CachingManager
import uz.mobile.joybox.datastore.LanguageDataStore


class LanguageViewModel(
    private val cachingManager: CachingManager,
    private val languageDataStore: LanguageDataStore,
) : ScreenModel {


    var selectedLanguage by mutableStateOf("")


    fun saveLanguage(lng: String) = screenModelScope.launch {
        cachingManager.saveLanguage(lng)
        languageDataStore.setLanguage(lng)
    }

    val language get() = cachingManager.getLanguage()

}