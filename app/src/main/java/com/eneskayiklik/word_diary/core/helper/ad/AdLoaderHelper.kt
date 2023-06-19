package com.eneskayiklik.word_diary.core.helper.ad

import android.content.Context
import androidx.core.os.bundleOf
import com.eneskayiklik.word_diary.BuildConfig
import com.eneskayiklik.word_diary.WordDiaryApp
import com.eneskayiklik.word_diary.util.extensions.hasInternetConnection
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdOptions.ADCHOICES_TOP_RIGHT
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdLoaderHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val analytics = Firebase.analytics

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getNativeAd(
        count: Int
    ): NativeAd? = suspendCancellableCoroutine { continuation ->
        if (context.hasInternetConnection().not() || WordDiaryApp.hasPremium) {
            continuation.resume(null, null)
            return@suspendCancellableCoroutine
        }
        val adLoader = AdLoader.Builder(context, BuildConfig.NATIVE_ADS_KEY)
            .forNativeAd { ad ->
                continuation.resume(ad, null)
            }
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    .setAdChoicesPlacement(ADCHOICES_TOP_RIGHT)
                    .build()
            )
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    continuation.resume(null, null)
                    analytics.logEvent(AD_LOAD_ERROR, bundleOf(
                        AD_LOAD_PARAM_MESSAGE to p0.message
                    ))
                }
            }).build()

        adLoader.loadAds(AdRequest.Builder().build(), count)
    }

    companion object {
        private const val AD_LOAD_ERROR = "ad_load_error"
        private const val AD_LOAD_PARAM_MESSAGE = "error_message"
    }
}