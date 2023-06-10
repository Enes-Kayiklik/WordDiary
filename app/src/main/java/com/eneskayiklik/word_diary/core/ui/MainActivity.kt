package com.eneskayiklik.word_diary.core.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.currentBackStackEntryAsState
import com.eneskayiklik.word_diary.core.ui.components.MainScaffold
import com.eneskayiklik.word_diary.core.ui.theme.MainViewModel
import com.eneskayiklik.word_diary.core.ui.theme.WordDiaryTheme
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import dagger.hilt.android.AndroidEntryPoint
import com.eneskayiklik.word_diary.core.data_store.data.UserPreference
import com.eneskayiklik.word_diary.core.tts.WordToSpeech
import com.eneskayiklik.word_diary.core.util.UiEvent
import com.eneskayiklik.word_diary.feature.NavGraphs
import com.eneskayiklik.word_diary.feature.appCurrentDestinationAsState
import com.eneskayiklik.word_diary.feature.destinations.ListsScreenDestination
import com.eneskayiklik.word_diary.feature.destinations.SettingsScreenDestination
import com.eneskayiklik.word_diary.feature.destinations.StatisticsScreenDestination
import com.eneskayiklik.word_diary.feature.startAppDestination
import com.eneskayiklik.word_diary.util.UpdateLanguage
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var tts: WordToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition {
            viewModel.keepScreen.value
        }
        super.onCreate(savedInstanceState)
        setContent {
            val userPrefs = viewModel.userPrefs.collectAsState(UserPreference()).value
            val themePrefs = userPrefs.themePrefs

            UpdateLanguage(iso = userPrefs.appLanguage.isoCode)

            WordDiaryTheme(
                color = Color(themePrefs.primaryColor),
                isAmoledBlack = themePrefs.isAmoledBlack,
                colorfulBackground = themePrefs.colorfulBackground,
                appTheme = themePrefs.appTheme,
                colorStyle = themePrefs.colorStyle,
                fontFamily = themePrefs.fontFamily.family
            ) {
                MainScreen(viewModel)
            }
        }
    }

    override fun onDestroy() {
        tts.shutdown()
        super.onDestroy()
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
@Composable
private fun MainScreen(
    viewModel: MainViewModel
) {
    val engine = rememberAnimatedNavHostEngine()
    val navController = engine.rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navController.appCurrentDestinationAsState().value
        ?: NavGraphs.root.startAppDestination

    val isBottomBarVisible by remember { derivedStateOf { shouldShowBottomBar(navBackStackEntry) } }

    val navigatePopUpTo: (String) -> Unit = remember {
        { route ->
            navController.navigate(route) {
                popUpTo(ListsScreenDestination.route)
                launchSingleTop = true
            }
        }
    }

    LaunchedEffect(key1 = viewModel) {
        viewModel.event.collectLatest {
            when (it) {
                is UiEvent.OnNavigate -> navController.navigate(it.route.route)
                else -> Unit
            }
        }
    }

    MainScaffold(
        modifier = Modifier
            .fillMaxSize(),
        showBottomBar = isBottomBarVisible,
        currentRoute = currentDestination.route,
        onNavigate = navigatePopUpTo
    ) {
        DestinationsNavHost(
            engine = engine,
            navController = navController,
            navGraph = NavGraphs.root
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun shouldShowBottomBar(backStackEntry: NavBackStackEntry?): Boolean {
    return backStackEntry?.destination?.route in listOf(
        StatisticsScreenDestination.route,
        ListsScreenDestination.route,
        SettingsScreenDestination.route,
    )
}