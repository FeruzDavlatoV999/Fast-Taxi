package uz.mobile.taxi.presentation.screens.naviagationActions

import cafe.adriel.voyager.navigator.Navigator
import uz.mobile.taxi.presentation.platform.MoviesAppScreen

fun <T> Navigator.popWithResult(obj: T) {
    val prev = if (items.size < 2) null else items[items.size - 2] as? MoviesAppScreen
    prev?.onResult(obj)
    pop()
}
