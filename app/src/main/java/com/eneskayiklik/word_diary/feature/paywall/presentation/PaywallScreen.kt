package com.eneskayiklik.word_diary.feature.paywall.presentation

import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.AddToDrive
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Diamond
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.ui.components.BasicDialog
import com.eneskayiklik.word_diary.core.ui.components.LottieAnimation
import com.eneskayiklik.word_diary.core.ui.components.PageIndicator
import com.eneskayiklik.word_diary.core.util.ScreensAnim
import com.eneskayiklik.word_diary.core.util.UiEvent
import com.eneskayiklik.word_diary.core.util.getDefaultAnimationSpec
import com.eneskayiklik.word_diary.feature.paywall.presentation.component.BenefitView
import com.eneskayiklik.word_diary.feature.paywall.presentation.component.UpgradePremiumButton
import com.eneskayiklik.word_diary.util.TITLE_LETTER_SPACING
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@OptIn(
    ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
@Destination(style = ScreensAnim::class)
fun PaywallScreen(
    navigator: DestinationsNavigator,
    viewModel: PaywallViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val state = viewModel.state.collectAsState().value
    val context = LocalContext.current

    val productsPagerState =
        rememberPagerState(initialPage = 1, pageCount = { state.products.size })

    val scaleAnim by animateFloatAsState(
        targetValue = if (state.products.isNotEmpty()) 1F else 0F,
        label = "products_anim",
        animationSpec = getDefaultAnimationSpec()
    )

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collectLatest {
            when (it) {
                UiEvent.ClearBackstack -> navigator.navigateUp()

                is UiEvent.ShowToast -> {
                    if (it.text != null) {
                        Toast.makeText(context, it.text, Toast.LENGTH_LONG).show()
                    } else if (it.textRes != null) {
                        Toast.makeText(context, it.textRes, Toast.LENGTH_LONG).show()
                    }
                }

                else -> Unit
            }
        }
    }

    if (state.isDialogActive) {
        when (state.dialogType) {
            PaywallDialog.SubscriptionSuccess -> BasicDialog(
                onDismiss = {
                    viewModel.onEvent(PaywallEvent.OnShowDialog(PaywallDialog.None))
                    navigator.navigateUp()
                },
                title = stringResource(id = R.string.purchase_dialog_title),
                description = stringResource(id = R.string.purchase_dialog_desc),
                dismissText = stringResource(id = R.string.purchase_dialog_dismiss),
                icon = Icons.Outlined.Diamond
            )

            else -> Unit
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(title = {
                Text(
                    text = stringResource(id = R.string.premium),
                    fontWeight = FontWeight.Medium,
                    letterSpacing = TITLE_LETTER_SPACING,
                    fontSize = 20.sp
                )
            }, actions = {
                TextButton(onClick = { viewModel.onEvent(PaywallEvent.OnRestore) }) {
                    Text(text = stringResource(id = R.string.restore_premium))
                }
            }, navigationIcon = {
                IconButton(onClick = { navigator.navigateUp() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            }, scrollBehavior = scrollBehavior)
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            LottieAnimation(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.BottomCenter)
                    .alpha(.5F),
                rawRes = R.raw.snow_anim
            )

            Icon(
                imageVector = Icons.Outlined.Diamond,
                contentDescription = null,
                modifier = Modifier
                    .graphicsLayer {
                        translationX = 96.dp.toPx()
                        translationY = 48.dp.toPx()
                    }
                    .alpha(.1F)
                    .align(Alignment.TopEnd)
                    .size(248.dp)
            )

            Icon(
                imageVector = Icons.Outlined.WorkspacePremium,
                contentDescription = null,
                modifier = Modifier
                    .graphicsLayer {
                        translationX = (-72).dp.toPx()
                        translationY = 24.dp.toPx()
                    }
                    .alpha(.1F)
                    .align(Alignment.CenterStart)
                    .size(196.dp)
            )

            Icon(
                imageVector = Icons.Outlined.StarOutline,
                contentDescription = null,
                modifier = Modifier
                    .graphicsLayer {
                        translationX = 92.dp.toPx()
                        translationY = (-24).dp.toPx()
                    }
                    .alpha(.1F)
                    .align(Alignment.BottomEnd)
                    .size(224.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.paywall_title),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                BenefitView(
                    startIcon = Icons.Outlined.Translate,
                    text = stringResource(id = R.string.unlimited_auto_translation),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(
                            MaterialTheme.shapes.extraSmall.copy(
                                topStart = MaterialTheme.shapes.medium.topStart,
                                topEnd = MaterialTheme.shapes.medium.topEnd
                            )
                        )
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(vertical = 12.dp, horizontal = 16.dp)
                )
                BenefitView(
                    startIcon = Icons.Outlined.AddToDrive,
                    text = stringResource(id = R.string.google_drive_backup),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.extraSmall)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(vertical = 12.dp, horizontal = 16.dp)
                )
                BenefitView(
                    startIcon = Icons.Outlined.Book,
                    text = stringResource(id = R.string.more_study_option),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.extraSmall)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(vertical = 12.dp, horizontal = 16.dp)
                )
                BenefitView(
                    startIcon = Icons.Outlined.Block,
                    text = stringResource(id = R.string.remove_ads),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.extraSmall)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(vertical = 12.dp, horizontal = 16.dp)
                )
                BenefitView(
                    startIcon = Icons.Outlined.Code,
                    text = stringResource(id = R.string.support_development),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(
                            MaterialTheme.shapes.extraSmall.copy(
                                bottomStart = MaterialTheme.shapes.medium.bottomStart,
                                bottomEnd = MaterialTheme.shapes.medium.bottomEnd
                            )
                        )
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(vertical = 12.dp, horizontal = 16.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1F))

            if (state.products.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(scaleX = scaleAnim, scaleY = 1F),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HorizontalPager(
                        state = productsPagerState
                    ) { currentPage ->
                        val currentItem = state.products[currentPage]

                        UpgradePremiumButton(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.outline,
                                    shape = MaterialTheme.shapes.medium
                                )
                                .padding(vertical = 4.dp),
                            selectedPriceText = currentItem.readableWeek,
                            titleText = currentItem.period,
                            buttonText = currentItem.continueButtonTitle,
                            onClick = {
                                viewModel.onEvent(
                                    PaywallEvent.OnMakePurchase(
                                        context,
                                        currentItem
                                    )
                                )
                            }
                        )
                    }

                    PageIndicator(
                        numberOfPages = state.products.size,
                        selectedPage = productsPagerState.currentPage,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.subscription_renews_automatically),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.primary)
                )
                Text(
                    text = stringResource(id = R.string.by_selecting_continue),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}