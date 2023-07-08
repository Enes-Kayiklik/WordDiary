package com.eneskayiklik.word_diary.feature.settings.presentation.about.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.util.DEVELOPER_MAIL
import com.eneskayiklik.word_diary.util.WORD_DIARY_GITHUB
import com.eneskayiklik.word_diary.util.WORD_DIARY_CROWDIN
import com.eneskayiklik.word_diary.util.LINKEDIN_ACCOUNT
import com.eneskayiklik.word_diary.util.TELEGRAM_CHANNEL
import com.eneskayiklik.word_diary.util.TELEGRAM_CHANNEL_REPORT_BUGS_TOPIC
import com.eneskayiklik.word_diary.util.TWITTER_ACCOUNT
import com.eneskayiklik.word_diary.util.WORD_DIARY_PLAY_STORE
import com.eneskayiklik.word_diary.util.extensions.openLink
import com.eneskayiklik.word_diary.util.extensions.sendEmail
import com.eneskayiklik.word_diary.util.extensions.shareAppLink

@Composable
fun AboutAppContent() {
    val context = LocalContext.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AboutTextButton(
            icon = painterResource(id = R.drawable.ic_telegram),
            title = stringResource(id = R.string.telegram)
        ) {
            context.openLink(TELEGRAM_CHANNEL)
        }
    }
}

@Composable
fun AboutDeveloperContent() {
    val context = LocalContext.current

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AboutTextButton(
                icon = painterResource(id = R.drawable.ic_github),
                title = stringResource(id = R.string.github)
            ) {
                context.openLink(WORD_DIARY_GITHUB)
            }
            AboutTextButton(
                icon = painterResource(id = R.drawable.ic_linkedin),
                title = stringResource(id = R.string.linkedin)
            ) {
                context.openLink(LINKEDIN_ACCOUNT)
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AboutTextButton(
                icon = painterResource(id = R.drawable.ic_at),
                title = stringResource(id = R.string.email)
            ) {
                context.sendEmail(DEVELOPER_MAIL)
            }
            AboutTextButton(
                icon = painterResource(id = R.drawable.ic_twitter),
                title = stringResource(id = R.string.twitter)
            ) {
                context.openLink(TWITTER_ACCOUNT)
            }
        }
    }
}

@Composable
fun SupportDevelopmentActions() {
    val context = LocalContext.current

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AboutTextButton(
                imageVector = Icons.Outlined.Translate,
                title = stringResource(id = R.string.translate)
            ) {
                context.openLink(WORD_DIARY_CROWDIN)
            }
            AboutTextButton(
                imageVector = Icons.Outlined.Star,
                title = stringResource(id = R.string.rate)
            ) {
                context.openLink(WORD_DIARY_PLAY_STORE)
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AboutTextButton(
                imageVector = Icons.Outlined.Share,
                title = stringResource(id = R.string.share)
            ) {
                context.shareAppLink()
            }
            /*AboutTextButton(
                imageVector = Icons.Outlined.Redeem,
                title = stringResource(id = R.string.donate)
            ) {

            }*/
            AboutTextButton(
                imageVector = Icons.Outlined.BugReport,
                title = stringResource(id = R.string.report_bugs)
            ) {
                context.openLink(TELEGRAM_CHANNEL_REPORT_BUGS_TOPIC)
            }
        }
    }
}