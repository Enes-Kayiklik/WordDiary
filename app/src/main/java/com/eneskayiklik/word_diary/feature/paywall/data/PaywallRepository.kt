package com.eneskayiklik.word_diary.feature.paywall.data

import android.app.Activity
import com.adapty.Adapty
import com.adapty.models.AdaptyPaywall
import com.adapty.models.AdaptyPaywallProduct
import com.adapty.models.AdaptyProfile
import com.adapty.utils.AdaptyResult
import com.eneskayiklik.word_diary.core.data.Result
import com.eneskayiklik.word_diary.feature.paywall.domain.PaywallRepository
import com.eneskayiklik.word_diary.feature.paywall.domain.model.toWordDiaryProduct
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

class PaywallRepositoryImpl @Inject constructor(

) : PaywallRepository {

    override suspend fun collectProducts() = flow {
        try {
            val paywall = getPaywall(DEFAULT_PAYWALL)
            val products = getProducts(paywall)
            val result = products.map { it.toWordDiaryProduct() }
            emit(Result.Success(result))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e))
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getPaywall(
        id: String
    ): AdaptyPaywall = suspendCancellableCoroutine { continuation ->
        Adapty.getPaywall(id) {
            when (it) {
                is AdaptyResult.Success -> continuation.resume(it.value, null)
                is AdaptyResult.Error -> continuation.resumeWithException(it.error)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getProducts(
        paywall: AdaptyPaywall
    ): List<AdaptyPaywallProduct> = suspendCancellableCoroutine { continuation ->
        Adapty.getPaywallProducts(paywall) {
            when (it) {
                is AdaptyResult.Success -> continuation.resume(it.value, null)
                is AdaptyResult.Error -> continuation.resumeWithException(it.error)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun makePurchase(
        activity: Activity,
        product: AdaptyPaywallProduct
    ): Result<AdaptyProfile?> = suspendCancellableCoroutine { continuation ->
        Adapty.makePurchase(
            activity = activity,
            product = product
        ) {
            when (it) {
                is AdaptyResult.Success -> continuation.resume(Result.Success(it.value), null)
                is AdaptyResult.Error -> continuation.resumeWithException(it.error)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun restorePurchase(): Result<AdaptyProfile?> =
        suspendCancellableCoroutine { continuation ->
            Adapty.restorePurchases {
                when (it) {
                    is AdaptyResult.Success -> continuation.resume(Result.Success(it.value), null)
                    is AdaptyResult.Error -> continuation.resumeWithException(it.error)
                }
            }
        }

    companion object {
        const val DEFAULT_PAYWALL = "default_paywall"
    }
}