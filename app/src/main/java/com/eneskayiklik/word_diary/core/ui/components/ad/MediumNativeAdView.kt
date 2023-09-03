package com.eneskayiklik.word_diary.core.ui.components.ad

import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageView
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.databinding.AdFullMediaBinding
import com.google.android.gms.ads.nativead.NativeAd

@Composable
fun MediumNativeAdView(
    modifier: Modifier = Modifier,
    onRemoveAds: () -> Unit,
    onVisibleOnScreen: () -> Unit,
    nativeAd: NativeAd
) {
    val colorScheme = MaterialTheme.colorScheme
    val removeAd = stringResource(id = R.string.remove_ads)

    AndroidViewBinding(factory = AdFullMediaBinding::inflate, modifier = modifier) {
        root.apply {
            mediaView = adMedia
            headlineView = adHeadline
            bodyView = adBody
            callToActionView = adCallToAction
            iconView = adAppIcon
            starRatingView = adStars
            advertiserView = adAdvertiser
        }

        adHeadline.text = nativeAd.headline
        adHeadline.setTextColor(colorScheme.onSecondaryContainer.toArgb())
        nativeAd.mediaContent?.let { adMedia.mediaContent = it }
        adMedia.setImageScaleType(ImageView.ScaleType.CENTER_CROP)

        if (nativeAd.body == null) {
            adBody.visibility = View.GONE
        } else {
            adBody.visibility = View.VISIBLE
            adBody.text = nativeAd.body
            adBody.setTextColor(colorScheme.onSurfaceVariant.toArgb())
        }

        if (nativeAd.callToAction == null) {
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

        removeAds.apply {
            setTextColor(colorScheme.primary.toArgb())
            strokeColor = ColorStateList.valueOf(colorScheme.outline.toArgb())
            text = removeAd
            textSize = 12.sp.value
            rippleColor = ColorStateList.valueOf(colorScheme.primary.copy(.1F).toArgb())
            setOnClickListener {
                onRemoveAds()
            }
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

        adIndicator.setTextColor(colorScheme.onSurfaceVariant.toArgb())
        root.setNativeAd(nativeAd)
    }

    LaunchedEffect(key1 = Unit) { onVisibleOnScreen() }
}