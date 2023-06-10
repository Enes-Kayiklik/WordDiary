package com.eneskayiklik.word_diary.util.extensions

fun <T, V> HashMap<T, V>.sumOf(selector: (V) -> Int): Int {
    var sum = 0
    for (element in this) {
        sum += selector(element.value)
    }
    return sum
}

fun <T, V> HashMap<T, V>.sumOfAll(selector: (T, V) -> Int): Int {
    var sum = 0
    for (element in this) {
        sum += selector(element.key, element.value)
    }
    return sum
}

fun <T, V> HashMap<T, V>.sumOfAllDouble(selector: (T, V) -> Double): Double {
    var sum = .0
    for (element in this) {
        sum += selector(element.key, element.value)
    }
    return sum
}