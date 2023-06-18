package com.eneskayiklik.word_diary.feature.paywall.domain.model

import com.adapty.models.AdaptyPaywallProduct
import com.adapty.models.AdaptyPeriodUnit
import java.math.BigDecimal

data class WordDiaryProduct(
    val currencySymbol: String,
    val periodUnit: AdaptyPeriodUnit,
    val numberOfUnits: Int,
    val period: String,
    val price: BigDecimal,
    val freeTrialPeriod: AdaptyPeriodUnit,
    val adaptyProduct: AdaptyPaywallProduct
) {
    private val perWeek = when (periodUnit) {
        AdaptyPeriodUnit.MONTH -> price / BigDecimal(numberOfUnits * 4)

        AdaptyPeriodUnit.YEAR -> price / BigDecimal(numberOfUnits * 54)

        else -> .0
    }

    val readablePrice = "$currencySymbol${price}"
    val readablePerWeek = "$currencySymbol${perWeek}"
}

fun AdaptyPaywallProduct.toWordDiaryProduct() = WordDiaryProduct(
    period = localizedSubscriptionPeriod ?: "",
    price = price,
    periodUnit = subscriptionPeriod?.unit ?: AdaptyPeriodUnit.UNKNOWN,
    numberOfUnits = subscriptionPeriod?.numberOfUnits ?: 0,
    freeTrialPeriod = freeTrialPeriod?.unit ?: AdaptyPeriodUnit.UNKNOWN,
    currencySymbol = currencySymbol,
    adaptyProduct = this
)