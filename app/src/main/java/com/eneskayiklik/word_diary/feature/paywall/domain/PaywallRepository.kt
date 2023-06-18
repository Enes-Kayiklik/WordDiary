package com.eneskayiklik.word_diary.feature.paywall.domain

import android.app.Activity
import com.adapty.models.AdaptyPaywall
import com.adapty.models.AdaptyPaywallProduct
import com.adapty.models.AdaptyProfile
import com.eneskayiklik.word_diary.feature.paywall.domain.model.WordDiaryProduct
import kotlinx.coroutines.flow.Flow
import com.eneskayiklik.word_diary.core.data.Result

interface PaywallRepository {

    suspend fun collectProducts(): Flow<Result<List<WordDiaryProduct>>>

    suspend fun getProducts(paywall: AdaptyPaywall): List<AdaptyPaywallProduct>

    suspend fun getPaywall(id: String): AdaptyPaywall

    suspend fun makePurchase(activity: Activity, product: AdaptyPaywallProduct): Result<AdaptyProfile?>

    suspend fun restorePurchase(): Result<AdaptyProfile?>
}