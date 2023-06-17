package com.eneskayiklik.word_diary.core.ui.components.ad

import android.content.res.ColorStateList
import android.view.View
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.eneskayiklik.word_diary.databinding.AdSmallMediaBinding
import com.google.android.gms.ads.nativead.NativeAd

@Composable
fun SmallNativeAdView(
    modifier: Modifier = Modifier,
    nativeAd: NativeAd,
    showActionButton: Boolean = false
) {
    val colorScheme = MaterialTheme.colorScheme

    AndroidViewBinding(factory = AdSmallMediaBinding::inflate, modifier = modifier) {
        root.apply {
            headlineView = adHeadline
            bodyView = adBody
            iconView = adAppIcon
            starRatingView = adStars
            advertiserView = adAdvertiser
        }

        adHeadline.text = nativeAd.headline
        adHeadline.setTextColor(colorScheme.onSecondaryContainer.toArgb())

        if (nativeAd.body == null) {
            adBody.visibility = View.GONE
        } else {
            adBody.visibility = View.VISIBLE
            adBody.text = nativeAd.body
            adBody.setTextColor(colorScheme.onSurfaceVariant.toArgb())
        }

        if (nativeAd.icon == null) {
            adAppIcon.visibility = View.GONE
        } else {
            adAppIcon.setImageDrawable(nativeAd.icon?.drawable)
            adAppIcon.visibility = View.VISIBLE
        }

        if (nativeAd.price == null) {
            adPrice.visibility = View.GONE
        } else {
            adPrice.visibility = View.VISIBLE
            adPrice.text = nativeAd.price!!.lowercase().replaceFirstChar { it.uppercase() }
            adPrice.setTextColor(colorScheme.onSurfaceVariant.toArgb())
        }

        if (nativeAd.starRating == null) {
            adStars.visibility = View.GONE
        } else {
            adStars.rating = nativeAd.starRating!!.toFloat()
            adStars.visibility = View.VISIBLE
            adStars.progressTintList = ColorStateList.valueOf(colorScheme.primary.toArgb())
            adStars.progressBackgroundTintList =
                ColorStateList.valueOf(colorScheme.primary.toArgb())
        }

        if (nativeAd.advertiser == null) {
            adAdvertiser.visibility = View.GONE
        } else {
            adAdvertiser.text = nativeAd.advertiser
            adAdvertiser.visibility = View.VISIBLE
            adAdvertiser.setTextColor(colorScheme.onSurfaceVariant.toArgb())
        }

        if (nativeAd.callToAction == null || showActionButton.not()) {
            adCallToAction.visibility = View.GONE
        } else {
            adCallToAction.apply {
                visibility = View.VISIBLE
                text = nativeAd.callToAction!!.lowercase().replaceFirstChar { it.uppercase() }
                setBackgroundColor(colorScheme.primary.toArgb())
                setTextColor(colorScheme.onPrimary.toArgb())
                textSize = 12.sp.value
            }
        }

        if (nativeAd.store == null) {
            adStore.visibility = View.GONE
        } else {
            adStore.text = nativeAd.store
            adStore.visibility = View.VISIBLE
            adStore.setTextColor(colorScheme.onSurfaceVariant.toArgb())
        }
        adIndicator.setTextColor(colorScheme.onSurfaceVariant.toArgb())
        root.setNativeAd(nativeAd)
    }
}