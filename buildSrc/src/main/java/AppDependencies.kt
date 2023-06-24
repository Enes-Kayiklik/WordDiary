import org.gradle.api.artifacts.dsl.DependencyHandler

object AppDependencies {
    val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"

    val coreLibraryDesugaring = "com.android.tools:desugar_jdk_libs:${Versions.desugaring}"
    val coreKtx = "androidx.core:core-ktx:${Versions.androidxCoreVersion}"
    val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleRuntime}"
    val activityCompose = "androidx.activity:activity-compose:${Versions.activityCompose}"
    val splashScreen = "androidx.core:core-splashscreen:${Versions.splashScreen}"
    val composeUi = "androidx.compose.ui:ui:${Versions.composeUiVersion}"
    val composeUiUtil = "androidx.compose.ui:ui-util:${Versions.composeUiVersion}"
    val composeFoundation = "androidx.compose.foundation:foundation:${Versions.composeUiVersion}"
    val composePreview = "androidx.compose.ui:ui-tooling-preview:${Versions.composeUiVersion}"
    val composeMaterial3 = "androidx.compose.material3:material3:${Versions.composeMaterialVersion}"
    val composeMaterialIcons = "androidx.compose.material:material-icons-extended:${Versions.composeUiVersion}"
    val composeAnimation = "androidx.compose.animation:animation-graphics:${Versions.composeUiVersion}"
    val composeViewBinding = "androidx.compose.ui:ui-viewbinding:${Versions.composeUiVersion}"

    val firebaseBom = "com.google.firebase:firebase-bom:${Versions.firebaseBom}"
    val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
    val analytics = "com.google.firebase:firebase-analytics-ktx"

    val accompanistPermission = "com.google.accompanist:accompanist-permissions:${Versions.accompanist_version}"

    val hiltGradle = "com.google.dagger:hilt-android:${Versions.hiltVersion}"
    val hiltKapt = "com.google.dagger:hilt-compiler:${Versions.hiltVersion}"
    val hiltNavigaitonCompose = "androidx.hilt:hilt-navigation-compose:${Versions.hiltNavigationComposeVersion}"

    val roomRuntime = "androidx.room:room-runtime:${Versions.roomVersion}"
    val roomKtx = "androidx.room:room-ktx:${Versions.roomVersion}"
    val roomCompiler = "androidx.room:room-compiler:${Versions.roomVersion}"
    val roomKapt = "androidx.room:room-compiler:${Versions.roomVersion}"

    val dataStore = "androidx.datastore:datastore:${Versions.dataStore}"
    val preferencesDataStore = "androidx.datastore:datastore-preferences:${Versions.dataStore}"

    val kotlinSerailization = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.kotlinSerailization}"

    val composeDesntinationCore = "io.github.raamcosta.compose-destinations:core:${Versions.composeDestinations}"
    val composeDesntinationAnimation = "io.github.raamcosta.compose-destinations:animations-core:${Versions.composeDestinations}"
    val composeDesntinationKsp = "io.github.raamcosta.compose-destinations:ksp:${Versions.composeDestinations}"

    val gradle = "com.android.tools.build:gradle:${Versions.gradle}"

    val cascadeDropdown = "me.saket.cascade:cascade-compose:${Versions.cascadeDropdown}"

    val composeDialogsCore = "com.maxkeppeler.sheets-compose-dialogs:core:${Versions.composeDialogs}"
    val composeDialogsColorPicker = "com.maxkeppeler.sheets-compose-dialogs:color:${Versions.composeDialogs}"
    val composeDialogsClockPicker = "com.maxkeppeler.sheets-compose-dialogs:clock:${Versions.composeDialogs}"

    val composeModalSheet = "io.github.oleksandrbalan:modalsheet:${Versions.composeModalSheet}"

    val oneSignal = "com.onesignal:OneSignal:${Versions.oneSignal}"
    val adsIdentifier = "com.google.android.gms:play-services-ads-identifier:${Versions.adsIdentifier}"

    val mpChart = "com.github.PhilJay:MPAndroidChart:${Versions.mpChart}"

    val adapty = "com.github.adaptyteam:AdaptySDK-Android:${Versions.adaptyVersion}"
    val lottie = "com.airbnb.android:lottie-compose:${Versions.lottie}"

    val ktorClient = "io.ktor:ktor-client-cio:${Versions.ktor}"
    val ktorCore = "io.ktor:ktor-client-core:${Versions.ktor}"
    val ktorLogging = "io.ktor:ktor-client-logging:${Versions.ktor}"
    val ktorContentNegotiation = "io.ktor:ktor-client-content-negotiation:${Versions.ktor}"
    val ktorJsonSerialization = "io.ktor:ktor-serialization-kotlinx-json:${Versions.ktor}"

    val androidxLibraries = listOf(
        coreKtx,
        lifecycleRuntime
    )

    val composeLibraries = listOf(
        activityCompose,
        composeUi,
        composeUiUtil,
        composeFoundation,
        composePreview,
        composeMaterial3,
        composeMaterialIcons,
        composeAnimation,
        composeViewBinding
    )

    val ktorLibraries = listOf(
        ktorClient,
        ktorCore,
        ktorLogging,
        ktorContentNegotiation,
        ktorJsonSerialization
    )
}

fun DependencyHandler.daggerHilt() {
    add("implementation", AppDependencies.hiltGradle)
    add("implementation", AppDependencies.hiltNavigaitonCompose)
    add("kapt", AppDependencies.hiltKapt)
}

fun DependencyHandler.composeDestinations() {
    add("implementation", AppDependencies.composeDesntinationCore)
    add("implementation", AppDependencies.composeDesntinationAnimation)
    add("ksp", AppDependencies.composeDesntinationKsp)
}

fun DependencyHandler.roomDb() {
    add("implementation", AppDependencies.roomRuntime)
    add("implementation", AppDependencies.roomKtx)
    add("kapt", AppDependencies.roomKapt)
    add("annotationProcessor", AppDependencies.roomCompiler)
}

fun DependencyHandler.kapt(list: List<String>) {
    list.forEach { dependency ->
        add("kapt", dependency)
    }
}

fun DependencyHandler.implementation(list: List<String>) {
    list.forEach { dependency ->
        add("implementation", dependency)
    }
}

fun DependencyHandler.androidTestImplementation(list: List<String>) {
    list.forEach { dependency ->
        add("androidTestImplementation", dependency)
    }
}

fun DependencyHandler.testImplementation(list: List<String>) {
    list.forEach { dependency ->
        add("testImplementation", dependency)
    }
}

fun DependencyHandler.firebaseThings() {
    add("implementation", AppDependencies.crashlytics)
    add("implementation", AppDependencies.analytics)
}