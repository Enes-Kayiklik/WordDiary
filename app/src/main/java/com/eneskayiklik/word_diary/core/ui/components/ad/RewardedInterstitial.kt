package com.eneskayiklik.word_diary.core.ui.components.ad

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.eneskayiklik.word_diary.BuildConfig
import com.google.android.gms.ads.LoadAdError

@Composable
fun rememberRewardedInterstitial(): RewardedInterstitialAd? {
    val context = LocalContext.current
    var rewardedInterstitialAd by remember { mutableStateOf<RewardedInterstitialAd?>(null) }

    LaunchedEffect(key1 = Unit) {
        RewardedInterstitialAd.load(context, BuildConfig.REWARDED_INTERSTITIAL_ADS_KEY,
            AdRequest.Builder().build(), object : RewardedInterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedInterstitialAd) {
                    rewardedInterstitialAd = ad
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    rewardedInterstitialAd = null
                }
            })
    }

    return rewardedInterstitialAd
}