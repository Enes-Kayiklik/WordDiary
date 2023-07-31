import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.devtools.ksp") version ("1.8.20-1.0.11")
    id("com.mikepenz.aboutlibraries.plugin")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    kotlin("plugin.serialization") version "1.8.20"
}

val versionFile = rootProject.file("version.properties")
if (versionFile.exists().not()) {
    versionFile.createNewFile()
    versionFile.bufferedWriter().use { out ->
        out.write("VERSION_CODE=1")
    }
}

val localProperties = Properties()
val versionProperties = Properties()
localProperties.load(FileInputStream(rootProject.file("local.properties")))
versionProperties.load(FileInputStream(versionFile))

val localVersionCode = versionProperties.getProperty("VERSION_CODE").toInt()

android {
    namespace = "com.eneskayiklik.word_diary"
    compileSdk = AppConfig.compileSdk
    buildToolsVersion = AppConfig.buildToolsVersion

    packagingOptions {
        resources.excludes.add("META-INF/DEPENDENCIES")
    }

    defaultConfig {
        applicationId = "com.eneskayiklik.word_diary"
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = localVersionCode
        versionName = "1.0.$localVersionCode"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        buildConfigField("String", "ADAPTY_KEY", localProperties.getProperty("adaptyKey"))
        buildConfigField(
            "String",
            "GOOGLE_CLIENT_ID",
            localProperties.getProperty("googleLoginWebClientId")
        )
        buildConfigField(
            "String",
            "ENCRYPTION_SECRET",
            localProperties.getProperty("encryptionSecret")
        )
        buildConfigField(
            "String",
            "ONE_SIGNAL_APP_ID",
            localProperties.getProperty("oneSignalAppId")
        )
        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "$projectDir/schemas"
            }
        }
    }

    androidComponents.onVariants { variant ->
        kotlin.sourceSets {
            getByName(variant.name).kotlin.srcDir("build/generated/ksp/${variant.name}/kotlin")
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
            buildConfigField(
                "String",
                "NATIVE_ADS_KEY",
                localProperties.getProperty("admobNativeProd")
            )
            buildConfigField(
                "String",
                "REWARDED_INTERSTITIAL_ADS_KEY",
                localProperties.getProperty("admobRewardedProd")
            )
        }
        getByName("debug") {
            versionNameSuffix = "-debug"
            buildConfigField(
                "String",
                "NATIVE_ADS_KEY",
                localProperties.getProperty("admobNativeDebug")
            )
            buildConfigField(
                "String",
                "REWARDED_INTERSTITIAL_ADS_KEY",
                localProperties.getProperty("admobRewardedDebug")
            )
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.6"
    }

    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }

    // Auto increment app version
    gradle.startParameter.taskNames.forEach {
        if (it.contains(":app:assembleRelease")) {
            versionFile.bufferedWriter().use { file ->
                file.write("VERSION_CODE=${(localVersionCode + 1)}")
            }
        }
    }
}

dependencies {
    implementation(project(":swiper"))

    implementation(AppDependencies.androidxLibraries)
    implementation(AppDependencies.composeLibraries)
    implementation(AppDependencies.splashScreen)
    testImplementation("junit:junit:4.+")

    // Accompanist permissions
    implementation(AppDependencies.accompanistPermission)

    // Ads Identifier
    implementation(AppDependencies.adsIdentifier)

    // One Signal
    implementation(AppDependencies.oneSignal)

    // Firebase
    implementation(platform(AppDependencies.firebaseBom))
    firebaseThings()

    // Ktor
    implementation(AppDependencies.ktorLibraries)

    // Hilt
    daggerHilt()

    // Room
    roomDb()

    // Data Store
    implementation(AppDependencies.dataStore)
    implementation(AppDependencies.preferencesDataStore)

    // Serialization
    implementation(AppDependencies.kotlinSerailization)

    // Google Login
    implementation(AppDependencies.googleAuth)

    // Nav Host
    implementation("com.google.accompanist:accompanist-navigation-animation:${Versions.accompanist_version}")
    implementation("com.google.accompanist:accompanist-navigation-material:${Versions.accompanist_version}")

    // Flow Layout
    implementation("com.google.accompanist:accompanist-flowlayout:${Versions.accompanist_version}")

    // Compose Destinations
    composeDestinations()

    // About Libraries
    implementation("com.mikepenz:aboutlibraries-core:${Versions.about_libraries}")

    // Monet
    implementation("com.github.Kyant0:Monet:0.1.0-alpha03")

    // Gson
    implementation("com.google.code.gson:gson:2.10")

    // Jsoup
    implementation("org.jsoup:jsoup:1.14.3")

    // Dropdown
    implementation(AppDependencies.cascadeDropdown)

    // Compose Dialogs
    implementation(AppDependencies.composeDialogsCore)
    implementation(AppDependencies.composeDialogsColorPicker)
    implementation(AppDependencies.composeDialogsClockPicker)
    implementation(AppDependencies.composeDialogList)

    // Mp charts
    implementation(AppDependencies.mpChart)

    coreLibraryDesugaring(AppDependencies.coreLibraryDesugaring)

    // Admob
    implementation("com.google.android.gms:play-services-ads:22.1.0")

    // Material 3
    implementation("com.google.android.material:material:1.9.0")

    // Adapty
    implementation(AppDependencies.adapty)

    // Lottie
    implementation(AppDependencies.lottie)

    // Coil
    implementation(AppDependencies.coil)

    // Drive
    implementation(AppDependencies.googleDriveLibraries)

    // Rich Text
    implementation(AppDependencies.richText)

    // Calendar
    implementation(AppDependencies.calendar)
}