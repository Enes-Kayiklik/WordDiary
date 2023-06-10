package com.eneskayiklik.swiper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember


/**
 * To control the Swiper.
 * Used to swipe card programmatically
 */
@Composable
fun rememberSwiperController(): SwiperController {
    return remember { SwiperControllerImpl() }
}

interface SwiperController {
    /**
     * Points to the top card's [CardController]
     */
    var currentCardController: CardController?
    fun swipeRight()
    fun swipeLeft()
}

class SwiperControllerImpl : SwiperController {
    override var currentCardController: CardController? = null

    override fun swipeRight() {
        currentCardController?.swipeRight()
    }

    override fun swipeLeft() {
        currentCardController?.swipeLeft()
    }
}