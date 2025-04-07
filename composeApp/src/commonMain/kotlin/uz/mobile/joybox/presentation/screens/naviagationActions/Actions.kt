package uz.mobile.joybox.presentation.screens.naviagationActions

import cafe.adriel.voyager.navigator.Navigator
import uz.mobile.joybox.presentation.screens.category.CategoryScreen
import uz.mobile.joybox.presentation.screens.detailsScreen.DetailsScreen
import uz.mobile.joybox.presentation.screens.live.LiveScreen
import uz.mobile.joybox.presentation.screens.profile.billing.PaymentScreen
import uz.mobile.joybox.presentation.screens.profile.billing.biilingScreen.BillingScreen
import uz.mobile.joybox.presentation.screens.profile.settings.SettingsScreen
import uz.mobile.joybox.presentation.screens.profile.settings.language.LanguageScreen
import uz.mobile.joybox.presentation.screens.profile.settings.privacy.OfertaScreen
import uz.mobile.joybox.presentation.screens.profile.settings.privacy.PrivacyScreen
import uz.mobile.joybox.presentation.screens.profile.settings.userUpdate.UserUpdateScreen

fun navigateToProfileItemsScreen(navigator: Navigator) {
    navigator.push(LanguageScreen())
}

fun navigateToSettingsItemsScreen(navigator: Navigator) {
    navigator.push(SettingsScreen())
}


fun navigateToUserUpdateScreen(navigator: Navigator) {
    navigator.push(UserUpdateScreen())
}

fun navigateToCategoryScreen(navigator: Navigator,id:Int,nameString: String) {
    navigator.push(CategoryScreen(id,nameString))
}

fun navigateToMovieDetailScreen(navigator: Navigator,id:Int,title:String) {
    navigator.push(DetailsScreen(id,title))
}

fun navigateToPrivacyScreen(navigator: Navigator) {
    navigator.push(PrivacyScreen())
}

fun navigateToOfertaScreen(navigator: Navigator) {
    navigator.push(OfertaScreen())
}

fun navigateToLiveScreen(navigator: Navigator) {
    navigator.push(LiveScreen())
}

fun navigateToPaymentScreen(navigator: Navigator) {
    navigator.push(PaymentScreen())
}

fun navigateToBillingScreen(navigator: Navigator, id:Int) {
    navigator.push(BillingScreen(id))
}