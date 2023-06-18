package com.eneskayiklik.word_diary.feature.paywall.presentation

import androidx.annotation.StringRes
import com.adapty.models.AdaptyPeriodUnit
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.feature.paywall.domain.model.WordDiaryProduct

data class PaywallState(
    val isLoading: Boolean = true,
    val products: List<WordDiaryProduct> = emptyList(),
    val selectedProduct: WordDiaryProduct? = null,
    val dialogType: PaywallDialog = PaywallDialog.None
) {
    @StringRes val continueButtonTitle =
        if (selectedProduct?.periodUnit == AdaptyPeriodUnit.MONTH && selectedProduct.numberOfUnits == 1) R.string.paywall_continue else R.string.start_free_trial

    val isDialogActive = dialogType != PaywallDialog.None
}

enum class PaywallDialog {
    SubscriptionSuccess,
    None
}
