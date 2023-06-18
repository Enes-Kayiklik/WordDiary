package com.eneskayiklik.word_diary.feature.paywall.presentation

import android.content.Context
import com.eneskayiklik.word_diary.feature.paywall.domain.model.WordDiaryProduct

sealed class PaywallEvent {

    data class OnShowDialog(val type: PaywallDialog) : PaywallEvent()
    data class OnMakePurchase(val context: Context) : PaywallEvent()
    data class OnSelectProduct(val product: WordDiaryProduct) : PaywallEvent()
    object OnRestore : PaywallEvent()
}
