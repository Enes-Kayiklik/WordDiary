package com.eneskayiklik.word_diary.core.data_store.data.model

data class CoinInfo(
    val dailyFreeCoin: Int = 0,
    val fromAdsCoin: Int = 0
) {
    val totalValue = "%,d".format(dailyFreeCoin + fromAdsCoin).replace(",", ".")
}