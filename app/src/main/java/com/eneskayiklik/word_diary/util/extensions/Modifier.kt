package com.eneskayiklik.word_diary.util.extensions

import android.app.Activity
import android.graphics.Rect
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.navigationBarPadding(): Modifier {
    val rectangle = remember { Rect() }
    val context = LocalView.current.context

    val window = (context as Activity).window

    window.decorView.getWindowVisibleDisplayFrame(rectangle)
    val navigationBarHeight = rectangle.top.pxToDp(context)

    return then(Modifier.padding(bottom = navigationBarHeight.dp))
}