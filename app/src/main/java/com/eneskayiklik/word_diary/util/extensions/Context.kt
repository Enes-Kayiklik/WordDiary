package com.eneskayiklik.word_diary.util.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

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

fun Context.sendEmail(address: String) {
   try {
       Intent(Intent.ACTION_SENDTO).apply {
           data = Uri.parse("mailto:")
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
