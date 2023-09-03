package com.eneskayiklik.word_diary.core.ad_manager

import com.google.android.gms.ads.nativead.NativeAd
import java.util.UUID

data class WordbookAd(
    val nativeAd: NativeAd,
    val seenCount: Int = 0,
    val isVisibleOnScreen: Boolean = false,
    val id: UUID = UUID.randomUUID()
)