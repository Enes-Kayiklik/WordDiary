import org.gradle.api.artifacts.dsl.DependencyHandler

object AppDependencies {
    val coreLibraryDesugaring = "com.android.tools:desugar_jdk_libs:${Versions.desugaring}"
    val coreKtx = "androidx.core:core-ktx:${Versions.androidxCoreVersion}"
    val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleRuntime}"
    val lifecycleRuntimeCompose =
        "androidx.lifecycle:lifecycle-runtime-compose:${Versions.lifecycleRuntime}"
    val lifecycleViewModelCompose =
        "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.lifecycleRuntime}"

    val activityCompose = "androidx.activity:activity-compose:${Versions.activityCompose}"
    val splashScreen = "androidx.core:core-splashscreen:${Versions.splashScreen}"
    val composeUi = "androidx.compose.ui:ui:${Versions.composeUiVersion}"
    val composeUiUtil = "androidx.compose.ui:ui-util:${Versions.composeUiVersion}"
    val composeFoundation = "androidx.compose.foundation:foundation:${Versions.composeUiVersion}"
    val composePreview = "androidx.compose.ui:ui-tooling-preview:${Versions.composeUiVersion}"
    val composeMaterial3 = "androidx.compose.material3:material3:${Versions.composeMaterialVersion}"
    val composeMaterialIcons =
        "androidx.compose.material:material-icons-extended:${Versions.composeUiVersion}"
    val composeAnimation =
        "androidx.compose.animation:animation-graphics:${Versions.composeUiVersion}"
    val composeViewBinding = "androidx.compose.ui:ui-viewbinding:${Versions.composeUiVersion}"

    val firebaseBom = "com.google.firebase:firebase-bom:${Versions.firebaseBom}"
    val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
    val analytics = "com.google.firebase:firebase-analytics-ktx"

    val googleAuth = "com.google.android.gms:play-services-auth:${Versions.googleAuth}"
    val googleDrive = "com.google.apis:google-api-services-drive:${Versions.driveApi}"
    val googleGson = "com.google.http-client:google-http-client-gson:${Versions.googleGson}"
    val googleClient = "com.google.api-client:google-api-client-android:${Versions.googleApiClient}"

    val accompanistPermission =
        "com.google.accompanist:accompanist-permissions:${Versions.accompanist_version}"

    val hiltGradle = "com.google.dagger:hilt-android:${Versions.hiltVersion}"
    val hiltKapt = "com.google.dagger:hilt-compiler:${Versions.hiltVersion}"
    val hiltNavigaitonCompose =
        "androidx.hilt:hilt-navigation-compose:${Versions.hiltNavigationComposeVersion}"

    val roomRuntime = "androidx.room:room-runtime:${Versions.roomVersion}"
    val roomKtx = "androidx.room:room-ktx:${Versions.roomVersion}"
    val roomCompiler = "androidx.room:room-compiler:${Versions.roomVersion}"
    val roomKapt = "androidx.room:room-compiler:${Versions.roomVersion}"

    val dataStore = "androidx.datastore:datastore:${Versions.dataStore}"
    val preferencesDataStore = "androidx.datastore:datastore-preferences:${Versions.dataStore}"

    val kotlinSerailization =
        "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.kotlinSerailization}"

    val composeDesntinationCore =
        "io.github.raamcosta.compose-destinations:core:${Versions.composeDestinations}"
    val composeDesntinationAnimation =
        "io.github.raamcosta.compose-destinations:animations-core:${Versions.composeDestinations}"
    val composeDesntinationKsp =
        "io.github.raamcosta.compose-destinations:ksp:${Versions.composeDestinations}"

    val cascadeDropdown = "me.saket.cascade:cascade-compose:${Versions.cascadeDropdown}"

    val composeDialogsCore =
        "com.maxkeppeler.sheets-compose-dialogs:core:${Versions.composeDialogs}"
    val composeDialogsColorPicker =
        "com.maxkeppeler.sheets-compose-dialogs:color:${Versions.composeDialogs}"
    val composeDialogsClockPicker =
        "com.maxkeppeler.sheets-compose-dialogs:clock:${Versions.composeDialogs}"
    val composeDialogList =
        "com.maxkeppeler.sheets-compose-dialogs:list:${Versions.composeDialogs}"

    val oneSignal = "com.onesignal:OneSignal:${Versions.oneSignal}"
    val adsIdentifier =
        "com.google.android.gms:play-services-ads-identifier:${Versions.adsIdentifier}"

    val mpChart = "com.github.PhilJay:MPAndroidChart:${Versions.mpChart}"

    val adapty = "com.github.adaptyteam:AdaptySDK-Android:${Versions.adaptyVersion}"
    val lottie = "com.airbnb.android:lottie-compose:${Versions.lottie}"

    val ktorClient = "io.ktor:ktor-client-cio:${Versions.ktor}"
    val ktorCore = "io.ktor:ktor-client-core:${Versions.ktor}"
    val ktorLogging = "io.ktor:ktor-client-logging:${Versions.ktor}"
    val ktorContentNegotiation = "io.ktor:ktor-client-content-negotiation:${Versions.ktor}"
    val ktorJsonSerialization = "io.ktor:ktor-serialization-kotlinx-json:${Versions.ktor}"
    val coil = "io.coil-kt:coil-compose:${Versions.coil}"

    val richText = "com.mohamedrejeb.richeditor:richeditor-compose:${Versions.richText}"

    val calendar = "com.kizitonwose.calendar:compose:${Versions.calendar}"
    val kotlinxDateTime = "org.jetbrains.kotlinx:kotlinx-datetime:${Versions.kotlinXDateTime}"

    val monet = "com.github.Kyant0:m3color:${Versions.monet}"

    val cloudy = "com.github.skydoves:cloudy:${Versions.cloudy}"

    val androidxLibraries = listOf(
        coreKtx,
        lifecycleRuntime,
        lifecycleRuntimeCompose,
        lifecycleViewModelCompose
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

    val googleDriveLibraries = listOf(
        googleGson,
        googleDrive,
        googleClient
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