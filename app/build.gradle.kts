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
    splits {

        // Configures multiple APKs based on screen density.
        abi {

            // Configures multiple APKs based on screen density.
            isEnable = true
            include("x86", "x86_64","armeabi-v7","arm64-v8a")
            exclude("mips64","riscv64","armeabi","mips")
        }
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
        create("0-without-sdk") {
            dimension = "version"
            applicationIdSuffix = ".base"
            versionNameSuffix = "-base"
        }

        // Including the core sdk libraries
        create("01-android-sdk") {
            // Assigns this product flavor to the "version" flavor dimension.
            // If you are using only one dimension, this property is optional,
            // and the plugin automatically assigns all the module's flavors to
            // that dimension.
            dimension = "version"
            applicationIdSuffix = ".core"
            versionNameSuffix = "-core"
        }
        // Including the base sdk and video view libraries.
        create("02-video-view") {
            dimension = "version"
            applicationIdSuffix = ".videoView"
            versionNameSuffix = "-videoView"
        }
        // Including the core sdk + virtual background
        create("03-virtual-background") {
            dimension = "version"
            applicationIdSuffix = ".vb"
            versionNameSuffix = "-vb"
        }

        create("04-video-filters") {
            dimension = "version"
            applicationIdSuffix = ".videoFilters"
            versionNameSuffix = "-videoFilters"
        }

        // Including the room kit with noise cancellation
        create("05-noise-cancellation") {
            dimension = "version"
            applicationIdSuffix = ".sdk_nc"
            versionNameSuffix = "-sdk_nc"
        }

        create("06-hls-player-stats") {
            dimension = "version"
            applicationIdSuffix = ".hls_player_stats"
            versionNameSuffix = "-hls_player_stats"
        }

        create("07-hls-player") {
            dimension = "version"
            applicationIdSuffix = ".hls_player"
            versionNameSuffix = "-hls_player"
        }

        // Including the room kit
        create("08-room-kit") {
            dimension = "version"
            applicationIdSuffix = ".roomkit"
            versionNameSuffix = "-roomkit"
        }

        create("09-room-kit-with-every-library") {
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

    "01-android-sdkImplementation"("live.100ms:android-sdk:$hmsVersion")

    "02-video-viewImplementation"("live.100ms:android-sdk:$hmsVersion")
    "02-video-viewImplementation"("live.100ms:video-view:$hmsVersion")

    "03-virtual-backgroundImplementation"("live.100ms:android-sdk:$hmsVersion")
    "03-virtual-backgroundImplementation"("live.100ms:virtual-background:$hmsVersion")

    "04-video-filtersImplementation"("live.100ms:android-sdk:$hmsVersion")
    "04-video-filtersImplementation"("live.100ms:video-filters:$hmsVersion")

    "05-noise-cancellationImplementation"("live.100ms:android-sdk:$hmsVersion")
    "05-noise-cancellationImplementation"("live.100ms:hms-noise-cancellation-android:$hmsVersion")

    "06-hls-player-statsImplementation"("live.100ms:android-sdk:$hmsVersion")
    "06-hls-player-statsImplementation"("live.100ms:hls-player-stats:$hmsVersion")

    "07-hls-playerImplementation"("live.100ms:android-sdk:$hmsVersion")
    "07-hls-playerImplementation"("live.100ms:hls-player:$hmsVersion")

    "08-room-kitImplementation"("live.100ms:room-kit:$roomKitVersion")

    "09-room-kit-with-every-libraryImplementation"("live.100ms:room-kit:$roomKitVersion")
    "09-room-kit-with-every-libraryImplementation"("live.100ms:video-view:$hmsVersion")
    "09-room-kit-with-every-libraryImplementation"("live.100ms:virtual-background:$hmsVersion")
    "09-room-kit-with-every-libraryImplementation"("live.100ms:video-filters:$hmsVersion")
    "09-room-kit-with-every-libraryImplementation"("live.100ms:hms-noise-cancellation-android:$hmsVersion")
    "09-room-kit-with-every-libraryImplementation"("live.100ms:hls-player-stats:$hmsVersion")
    "09-room-kit-with-every-libraryImplementation"("live.100ms:hls-player:$hmsVersion")

}

