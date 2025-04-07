-keepclassmembers class uz.mobile.joybox.data.remote.dto.base.** {
    *** Companion;
}
-keepclassmembers class uz.mobile.joybox.data.remote.dto.paging.** {
    *** Companion;
}
-keepclassmembers class uz.mobile.joybox.data.remote.dto.** {
    *** Companion;
}
-keepclassmembers class uz.mobile.joybox.data.remote.** {
    *** Companion;
}
-keepclassmembers class uz.mobile.joybox.domain.model.** {
    *** Companion;
}
-keepclassmembers class uz.mobile.joybox.domain.util.** {
    *** Companion;
}
-keepclassmembers class uz.mobile.joybox.domain.util.camera.** {
    *** Companion;
}
-keepclassmembers class uz.mobile.joybox.domain.validation.** {
    *** Companion;
}

-keepclassmembers class uz.mobile.joybox.datastore.** {
    *** Companion;
}

-keepclassmembers class uz.mobile.joybox.data.repository.** {
    *** Companion;
}

-keepclassmembers class uz.mobile.joybox.data.remote.service.** {
    *** Companion;
}

-keepclassmembers class uz.mobile.joybox.presentation.screens.home.** {
    *** Companion;
}

-keepclassmembers class uz.mobile.joybox.presentation.screens.profile.settings.language.** {
    *** Companion;
}

-keepclassmembers class uz.mobile.joybox.data.remote.** {
    *** Companion;
}

# Kotlin serialization looks up the generated serializer classes through a function on companion
# objects. The companions are looked up reflectively so we need to explicitly keep these functions.
-keepclasseswithmembers class **.*$Companion {
    kotlinx.serialization.KSerializer serializer(...);
}

-dontwarn com.google.gson.Gson
-dontwarn com.google.gson.JsonIOException
-dontwarn com.google.gson.JsonSyntaxException
-dontwarn com.google.gson.reflect.TypeToken

# Ktor
-keep class io.ktor.** { *; }
-keep class kotlinx.coroutines.** { *; }
-dontwarn com.typesafe.**
-dontwarn org.slf4j.**

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { <fields>; }

# Prevent proguard from stripping interface information from TypeAdapter, TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# Retain generic signatures of TypeToken and its subclasses with R8 version 3.0 and higher.
-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken

##---------------End: proguard configuration for Gson  ----------