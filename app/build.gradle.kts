plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "live.hms.referencenewactivity"
    compileSdk = 34

    defaultConfig {
        applicationId = "live.hms.referencenewactivity"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }

        getByName("debug") {
            applicationIdSuffix = ".debug"
            isDebuggable = true
        }
//        create("vb") {
//            initWith(getByName("debug"))
//            applicationIdSuffix = ".vb"
//        }
    }
    flavorDimensions += "version"
    productFlavors {
        // Sample apk with no 100ms libraries
        create("base") {
            dimension = "version"
            applicationIdSuffix = ".base"
            versionNameSuffix = "-base"
        }

        // Including the core sdk libraries
        create("a") {
            // Assigns this product flavor to the "version" flavor dimension.
            // If you are using only one dimension, this property is optional,
            // and the plugin automatically assigns all the module's flavors to
            // that dimension.
            dimension = "version"
            applicationIdSuffix = ".core"
            versionNameSuffix = "-core"
        }
        // Including the base sdk and video view libraries.
        create("b") {
            dimension = "version"
            applicationIdSuffix = ".videoView"
            versionNameSuffix = "-videoView"
        }
        // Including the core sdk + virtual background
        create("c") {
            dimension = "version"
            applicationIdSuffix = ".vb"
            versionNameSuffix = "-vb"
        }

        create("d") {
            dimension = "version"
            applicationIdSuffix = ".videoFilters"
            versionNameSuffix = "-videoFilters"
        }

        // Including the room kit with noise cancellation
        create("e") {
            dimension = "version"
            applicationIdSuffix = ".sdk_nc"
            versionNameSuffix = "-sdk_nc"
        }

        create("f") {
            dimension = "version"
            applicationIdSuffix = ".hls_player_stats"
            versionNameSuffix = "-hls_player_stats"
        }

        create("g") {
            dimension = "version"
            applicationIdSuffix = ".hls_player"
            versionNameSuffix = "-hls_player"
        }

        // Including the room kit
        create("h") {
            dimension = "version"
            applicationIdSuffix = ".roomkit"
            versionNameSuffix = "-roomkit"
        }

        create("i") {
            dimension = "version"
            applicationIdSuffix = ".all"
            versionNameSuffix = "-all"
        }

    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    val hmsVersion = "2.9.55"
    val roomKitVersion = "1.2.11"

    "aImplementation"("live.100ms:android-sdk:$hmsVersion")

    "bImplementation"("live.100ms:android-sdk:$hmsVersion")
    "bImplementation"("live.100ms:video-view:$hmsVersion")

    "cImplementation"("live.100ms:android-sdk:$hmsVersion")
    "cImplementation"("live.100ms:virtual-background:$hmsVersion")

    "dImplementation"("live.100ms:android-sdk:$hmsVersion")
    "dImplementation"("live.100ms:video-filters:$hmsVersion")

    "eImplementation"("live.100ms:android-sdk:$hmsVersion")
    "eImplementation"("live.100ms:hms-noise-cancellation-android:$hmsVersion")

    "fImplementation"("live.100ms:android-sdk:$hmsVersion")
    "fImplementation"("live.100ms:hls-player-stats:$hmsVersion")

    "gImplementation"("live.100ms:android-sdk:$hmsVersion")
    "gImplementation"("live.100ms:hls-player:$hmsVersion")

    "hImplementation"("live.100ms:room-kit:$roomKitVersion")

    "iImplementation"("live.100ms:room-kit:$roomKitVersion")
    "iImplementation"("live.100ms:video-view:$hmsVersion")
    "iImplementation"("live.100ms:virtual-background:$hmsVersion")
    "iImplementation"("live.100ms:video-filters:$hmsVersion")
    "iImplementation"("live.100ms:hms-noise-cancellation-android:$hmsVersion")
    "iImplementation"("live.100ms:hls-player-stats:$hmsVersion")
    "iImplementation"("live.100ms:hls-player:$hmsVersion")

}