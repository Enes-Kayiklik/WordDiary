package com.eneskayiklik.word_diary.util.extensions

inline fun <T> List<T>.filterIf(filter: () -> Boolean, predicate: (T) -> Boolean): List<T> {
    return if (filter()) filterTo(ArrayList(), predicate)
    else this
}

fun CharSequence.containsAny(
    list: List<CharSequence>,
    ignoreCase: Boolean = true
) = list.any { it.contains(this, ignoreCase) }

fun <T> Iterable<T>.takeRandom(n: Int, notEqualTo: List<T> = emptyList()): List<T> {
    return filter { it !in notEqualTo }.shuffled().take(n)
}