package com.eneskayiklik.word_diary.util.extensions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.provider.Settings
import com.eneskayiklik.word_diary.BuildConfig
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.feature.statistics.presentation.StatisticsState
import com.eneskayiklik.word_diary.util.DEVELOPER_MAIL
import com.eneskayiklik.word_diary.util.WORD_DIARY_PLAY_STORE
import com.eneskayiklik.word_diary.util.WORD_DIARY_PLAY_STORE_SHORT
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.Scope
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import java.util.Collections

fun Context.vibratePhone(time: Long) {
    try {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(time, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(time)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.googleLoginClient(): GoogleSignInClient {
    val signInOptions = GoogleSignInOptions.Builder()
        .requestScopes(
            Scope("https://www.googleapis.com/auth/drive.file"),
            Scope("https://www.googleapis.com/auth/drive.appdata")
        )
        .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
        .requestEmail()
        .requestProfile()
        .build()
    return GoogleSignIn.getClient(this, signInOptions)
}

fun Context.getDriveService(): Drive {
    val signedInAccount = GoogleSignIn.getLastSignedInAccount(this)
    val credential = GoogleAccountCredential
        .usingOAuth2(this, Collections.singleton(Scopes.DRIVE_FILE))
        .apply {
            selectedAccount = signedInAccount?.account
        }

    return Drive.Builder(
        NetHttpTransport(),
        GsonFactory.getDefaultInstance(),
        credential
    ).setApplicationName(
        getString(R.string.app_name)
    ).build()
}

fun Context.openLink(link: String) {
    try {
        Intent(Intent.ACTION_VIEW).apply {
            val trim = link.trim()
            val newLink = if (trim.startsWith("www.")) "https://$trim" else trim
            data = Uri.parse(newLink)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(this)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.shareAppLink() {
    try {
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, WORD_DIARY_PLAY_STORE)
            startActivity(Intent.createChooser(this, "Share"))
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.sendEmail(address: String) {
    try {
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$DEVELOPER_MAIL")
            putExtra(Intent.EXTRA_EMAIL, address)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(this)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.openTTSSettings() {
    try {
        Intent().apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            action = "com.android.settings.TTS_SETTINGS"
            startActivity(this)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}

fun Context.shareStatistics(statisticsState: StatisticsState) {
    val learningWordText =
        "${statisticsState.learningWordCount} " + if (statisticsState.learningWordCount <= 1) getString(
            R.string.word_singular
        )
        else getString(R.string.word_plural)

    val completeLearnedText =
        "${statisticsState.completeLearnedWordCount} " + if (statisticsState.completeLearnedWordCount <= 1) getString(
            R.string.word_singular
        )
        else getString(R.string.word_plural)

    val shareText = """
        📅 ${getString(R.string.start_of_learning)}, ${statisticsState.startOfLearning}
        ⭐ $learningWordText ${getString(R.string.learned)}
        📘 $completeLearnedText ${getString(R.string.learning)}
        🔥 ${getString(R.string.best_streak)} ${getString(statisticsState.maxStreakFormatter, statisticsState.maxStreakCount)}
        🕔 ${statisticsState.allTimeStudyTime} ${getString(R.string.study_time)}
        🔬 ${statisticsState.allTimeStudySessions} ${getString(R.string.study_sessions)}
        
        📱${getString(R.string.download_now)} $WORD_DIARY_PLAY_STORE_SHORT
    """.trimIndent()

    try {
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(this)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.hasInternetConnection(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val activeNetwork = connectivityManager.activeNetwork ?: return false
    val capabilities =
        connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

    return when {
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> true
    }
}

fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("no activity")
}

fun Context.finishActivity() {
    try {
        findActivity().finish()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.restartApp() {
    try {
        findActivity().finish()
        packageManager.getLaunchIntentForPackage(packageName)?.apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(this)
        }
        findActivity().finish()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}