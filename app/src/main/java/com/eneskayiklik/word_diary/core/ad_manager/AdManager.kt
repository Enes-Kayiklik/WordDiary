package com.eneskayiklik.word_diary.core.ad_manager

import android.content.Context
import androidx.core.os.bundleOf
import com.eneskayiklik.word_diary.BuildConfig
import com.eneskayiklik.word_diary.WordDiaryApp
import com.eneskayiklik.word_diary.util.ConnectivityObserver
import com.eneskayiklik.word_diary.util.NetworkConnectivityObserver
import com.eneskayiklik.word_diary.util.extensions.hasInternetConnection
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

object AdManager {

    private var isActivated: Boolean = false
    private val analytics = Firebase.analytics
    private val defaultScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private var connectivityObserver: NetworkConnectivityObserver? = null

    private val _event = MutableSharedFlow<Boolean>()
    val activeAds = MutableStateFlow<List<WordbookAd>>(emptyList())

    fun createAdManager(context: Context) {
        if (isActivated) return

        connectivityObserver = NetworkConnectivityObserver(context)
        startCollect(context)

        isActivated = true
    }

    fun increaseSeenCount(adId: UUID) = defaultScope.launch {
        activeAds.update { ads ->
            ads.map { ad ->
                if (ad.id == adId) ad.copy(
                    seenCount = ad.seenCount + 1
                ) else ad
            }
        }
    }

    fun borrowOrRelease(adId: UUID?, isShown: Boolean) = defaultScope.launch {
        if (adId == null) return@launch

        activeAds.update { ads ->
            ads.map { ad ->
                if (ad.id == adId) ad.copy(
                    isVisibleOnScreen = isShown
                ) else ad
            }
        }

        if (isShown.not()) {
            activeAds.update { ads ->
                var hasChanged = false
                val newArr = ads.mapNotNull {
                    if (it.seenCount < 8) {
                        it
                    } else {
                        hasChanged = true
                        it.nativeAd.destroy()
                        null
                    }
                }

                _event.emit(newArr.size <= 2 && hasChanged)
                return@update newArr
            }
        }
    }

    fun release() = defaultScope.launch {
        connectivityObserver = null
        activeAds.update {
            it.forEach { ad ->
                ad.nativeAd.destroy()
            }
            return@update emptyList()
        }
    }

    private fun startCollect(context: Context) {
        connectivityObserver?.let { it() }?.onEach { status ->
            if (status == ConnectivityObserver.Status.Available
                && activeAds.value.isEmpty()
                ) loadAd(context, 3)
        }?.launchIn(defaultScope)

        _event.onEach {
            if (it) loadAd(context, 2)
        }.launchIn(defaultScope)
    }

    private suspend fun loadAd(context: Context, count: Int) = withContext(Dispatchers.Main) {
        if (context.hasInternetConnection().not() || WordDiaryApp.hasPremium) {
            return@withContext
        }
        val adLoader = AdLoader.Builder(context, BuildConfig.NATIVE_ADS_KEY)
            .forNativeAd { ad ->
                activeAds.update {
                    it + WordbookAd(
                        nativeAd = ad
                    )
                }
            }
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT)
                    .build()
            )
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    analytics.logEvent(
                        AD_LOAD_ERROR, bundleOf(
                            AD_LOAD_PARAM_MESSAGE to p0.message
                        )
                    )
                }
            }).build()

        adLoader.loadAds(AdRequest.Builder().build(), count)
    }

    const val AD_LOAD_ERROR = "ad_load_error"
    const val AD_LOAD_PARAM_MESSAGE = "error_message"
}