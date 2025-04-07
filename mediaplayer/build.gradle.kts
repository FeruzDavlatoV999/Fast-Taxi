import org.jetbrains.compose.resources.ResourcesExtension

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    jvmToolchain(11)
    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        all {
            languageSettings.optIn("androidx.compose.foundation.ExperimentalFoundationApi")
        }

        commonMain.dependencies {
            implementation(compose.foundation)
            implementation(compose.runtime)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.components.resources)

            implementation(libs.image.loader)
            implementation(libs.compose.webview)
        }

        iosMain.dependencies {
        }


        androidMain.dependencies {
            implementation(libs.androidx.core)
            implementation(libs.androidx.media.exoplayer.core)
            implementation(libs.androidx.media.ui)
            implementation(libs.androidx.media.exoplayer.hls)
            implementation(libs.androidx.lifecycle.process)

            implementation(libs.androidx.media.datasource)
            implementation(libs.androidx.media.okhttp)
            implementation(libs.androidx.media.dash)
            implementation(libs.androidx.media.database)


        }
    }
}

android {
    namespace = "ComposeMultiplatformMediaPlayer.org"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "reelsdemo.composemultiplatformmediaplayer.generated.resources"
    generateResClass = ResourcesExtension.ResourceClassGeneration.Always
}
