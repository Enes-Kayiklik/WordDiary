package com.eneskayiklik.word_diary

import org.junit.Test
import com.eneskayiklik.word_diary.util.extensions.formatStudyTimer
import junit.framework.TestCase.assertEquals

class TimeUtilsTest {

    @Test
    fun test_timer_second() {
        val actual = 25000L.formatStudyTimer()
        val expected = "00:25"
        assertEquals("test_timer_second failed", expected, actual)
    }

    @Test
    fun test_timer_minute() {
        val actual = 64000L.formatStudyTimer()
        val expected = "01:04"
        assertEquals("test_timer_minute failed", expected, actual)
    }

    @Test
    fun test_timer_hour() {
        val actual = 6445407L.formatStudyTimer()
        val expected = "1:47:25"
        assertEquals("test_timer_hour failed", expected, actual)
    }

    @Test
    fun test_timer_mix() {
        val actual = 64454070L.formatStudyTimer()
        val expected = "17:54:14"
        assertEquals("test_timer_mix failed", expected, actual)
    }
}