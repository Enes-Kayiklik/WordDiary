package com.eneskayiklik.word_diary.util.extensions

fun String.takeHint(
    actualWord: String
): String {
    var i = 0
    while (actualWord.getOrNull(i)?.lowercase() == getOrNull(i)?.lowercase()) {
        i++
    }
    return actualWord.take(i + 1)
}