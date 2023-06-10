package com.eneskayiklik.word_diary.feature.onboarding.presentation

data class OnboardingState(
    val pages: List<OnboardingPage> = listOf(
        OnboardingPage.LanguageSelect,
        OnboardingPage.TextToSpeechSetup,
        OnboardingPage.AlarmSetup
    )
)

enum class OnboardingPage {
    LanguageSelect,
    TextToSpeechSetup,
    AlarmSetup
}
