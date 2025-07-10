package uz.mobile.taxi.datastore

import platform.Foundation.NSUserDefaults

actual fun changeLang(lang: String) {
    NSUserDefaults.standardUserDefaults.setObject(arrayListOf(lang), "AppleLanguages")
}