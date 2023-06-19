package com.eneskayiklik.word_diary.feature.settings.presentation.about.component

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.LocalLibrary
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.feature.destinations.LicensesScreenDestination
import com.eneskayiklik.word_diary.util.PRIVACY
import com.eneskayiklik.word_diary.util.TERMS
import com.eneskayiklik.word_diary.util.extensions.openLink
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import me.saket.cascade.CascadeDropdownMenu

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AboutDropdownMenu(
    expanded: Boolean,
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    CascadeDropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        modifier = modifier,
        offset = DpOffset(x = 16.dp, y = 0.dp)
    ) {
        DropdownMenuItem(
            text = { Text(stringResource(id = R.string.destination_licenses)) },
            onClick = {
                onDismiss()
                navigator.navigate(LicensesScreenDestination)
            },
            leadingIcon = {
                Icon(
                    Icons.Outlined.LocalLibrary,
                    contentDescription = null
                )
            }
        )
        DropdownMenuItem(
            text = { Text(stringResource(id = R.string.settings_privacy_policy)) },
            onClick = {
                onDismiss()
                context.openLink(PRIVACY)
            },
            leadingIcon = {
                Icon(
                    Icons.Outlined.Policy,
                    contentDescription = null
                )
            }
        )
        DropdownMenuItem(
            text = { Text(stringResource(id = R.string.settings_terms_conditions)) },
            onClick = {
                onDismiss()
                context.openLink(TERMS)
            },
            leadingIcon = {
                Icon(
                    Icons.Outlined.Description,
                    contentDescription = null
                )
            }
        )
    }
}