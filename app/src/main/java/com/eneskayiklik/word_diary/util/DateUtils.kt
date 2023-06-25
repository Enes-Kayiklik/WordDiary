package com.eneskayiklik.word_diary.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateUtils {

    fun getToday(pattern: String = "dd.MM.yyyy") =
        LocalDate.now().format(DateTimeFormatter.ofPattern(pattern))
}