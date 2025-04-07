package uz.mobile.joybox.datastore

class LanguageDataStore {

    var language: String = "uz"
        private set

    fun setLanguage(lng: String) {
        language = lng
        changeLang(lng)
    }
}