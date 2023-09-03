package com.eneskayiklik.word_diary.feature.paywall.presentation

import com.eneskayiklik.word_diary.feature.paywall.domain.model.WordDiaryProduct

data class PaywallState(
    val isLoading: Boolean = true,
    val products: List<WordDiaryProduct> = emptyList(),
    val dialogType: PaywallDialog = PaywallDialog.None,
    val isOtherOptionsVisible: Boolean = false
) {
    val isDialogActive = dialogType != PaywallDialog.None
}

enum class PaywallDialog {
    SubscriptionSuccess,
    None
}
