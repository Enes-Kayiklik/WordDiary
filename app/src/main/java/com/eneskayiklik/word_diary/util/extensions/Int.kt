package com.eneskayiklik.word_diary.util.extensions

import android.content.Context

fun Int.pxToDp(context: Context) = this / context.resources.displayMetrics.density
fun Int.dpToPx(context: Context) = this * context.resources.displayMetrics.density

fun Float.pxToDp(context: Context) = this / context.resources.displayMetrics.density
fun Float.dpToPx(context: Context) = this * context.resources.displayMetrics.density