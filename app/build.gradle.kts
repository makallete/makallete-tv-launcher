plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "tv.makallete.launcher"
    compileSdk = 34

    defaultConfig {
        applicationId = "tv.makallete.launcher"
        minSdk = 30
        // The TV runs Android 11; targeting it keeps Android 11 behavior and avoids newer FGS-type rules.
        targetSdk = 30
        versionCode = 1
        versionName = "0.1.0-alpha"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            // Unsigned unless a private keystore is supplied through the environment.
            val store = System.getenv("RELEASE_STORE_FILE")
            if (!store.isNullOrBlank()) {
                signingConfig = signingConfigs.create("privateRelease") {
                    storeFile = file(store)
                    storePassword = System.getenv("RELEASE_STORE_PASSWORD")
                    keyAlias = System.getenv("RELEASE_KEY_ALIAS")
                    keyPassword = System.getenv("RELEASE_KEY_PASSWORD")
                }
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    lint {
        // Sideload-only Android 11 app; Play Store submission is outside this release.
        disable += "ExpiredTargetSdkVersion"
        abortOnError = true
        checkReleaseBuilds = true
    }
}
