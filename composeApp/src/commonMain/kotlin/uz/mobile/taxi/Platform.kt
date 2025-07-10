package uz.mobile.taxi

import org.koin.core.module.Module

interface Platform {
    val name: String
}

enum class System{
    IOS, ANDROID
}

expect fun getSystem() : System

expect fun getPlatform(): Platform

expect fun platformModule(): Module

expect fun cachingModule(): Module

enum class Orientation{
    PORTRAIT,
    LANDSCAPE,
    UNDEFINED
}

expect fun getScreenOrientation(): Orientation

expect fun onApplicationStartPlatformSpecific()

expect fun logMessage(message: String)


