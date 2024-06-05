import java.io.FileInputStream
import java.util.*

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    signing
}
android {
    signingConfigs {
        register("release") {

            val keystorePropertiesFile = file("../sample-keystore.properties")

            if (!keystorePropertiesFile.exists()) {
                logger.warn("Release builds may not work: signing config not found.")
                return@register
            }

            val keystoreProperties = Properties()
            keystoreProperties.load(FileInputStream(keystorePropertiesFile))

            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
        }
    }
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

        // Including the room kit
        create("01-room-kit") {
            dimension = "version"
            applicationIdSuffix = ".roomkit"
            versionNameSuffix = "-roomkit"
        }

        // Including the core sdk libraries
        create("02-android-sdk") {
            // Assigns this product flavor to the "version" flavor dimension.
            // If you are using only one dimension, this property is optional,
            // and the plugin automatically assigns all the module's flavors to
            // that dimension.
            dimension = "version"
            applicationIdSuffix = ".core"
            versionNameSuffix = "-core"
        }
        // Including the base sdk and video view libraries.
        create("03-video-view") {
            dimension = "version"
            applicationIdSuffix = ".videoView"
            versionNameSuffix = "-videoView"
        }
        // Including the core sdk + virtual background
        create("04-virtual-background") {
            dimension = "version"
            applicationIdSuffix = ".vb"
            versionNameSuffix = "-vb"
        }

        // Including the room kit with noise cancellation
        create("05-noise-cancellation") {
            dimension = "version"
            applicationIdSuffix = ".sdk_nc"
            versionNameSuffix = "-sdk_nc"
        }

        create("06-video-filters") {
            dimension = "version"
            applicationIdSuffix = ".videoFilters"
            versionNameSuffix = "-videoFilters"
        }

        create("07-hls-player") {
            dimension = "version"
            applicationIdSuffix = ".hls_player"
            versionNameSuffix = "-hls_player"
        }

        create("08-hls-player-stats") {
            dimension = "version"
            applicationIdSuffix = ".hls_player_stats"
            versionNameSuffix = "-hls_player_stats"
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

    val hmsVersion = "2.9.59"
    val roomKitVersion = "1.2.13"
    
    "01-room-kitImplementation"("live.100ms:room-kit:$roomKitVersion")
    
    "02-android-sdkImplementation"("live.100ms:android-sdk:$hmsVersion")

    "03-video-viewImplementation"("live.100ms:android-sdk:$hmsVersion")
    "03-video-viewImplementation"("live.100ms:video-view:$hmsVersion")

    "04-virtual-backgroundImplementation"("live.100ms:android-sdk:$hmsVersion")
    "04-virtual-backgroundImplementation"("live.100ms:virtual-background:$hmsVersion")
    
    "05-noise-cancellationImplementation"("live.100ms:android-sdk:$hmsVersion")
    "05-noise-cancellationImplementation"("live.100ms:hms-noise-cancellation-android:$hmsVersion")

    "06-video-filtersImplementation"("live.100ms:android-sdk:$hmsVersion")
    "06-video-filtersImplementation"("live.100ms:video-filters:$hmsVersion")

    "07-hls-playerImplementation"("live.100ms:android-sdk:$hmsVersion")
    "07-hls-playerImplementation"("live.100ms:hls-player:$hmsVersion")

    "08-hls-player-statsImplementation"("live.100ms:android-sdk:$hmsVersion")
    "08-hls-player-statsImplementation"("live.100ms:hls-player-stats:$hmsVersion")
    
}

