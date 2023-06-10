package com.eneskayiklik.word_diary

import com.eneskayiklik.word_diary.util.extensions.formatFloating
import junit.framework.TestCase
import org.junit.Test

class NumberUtilsTest {

    @Test
    fun test_take_hint_1() {
        val userInput = 78.2
        val expected = 78.2
        val actual = userInput.formatFloating()

        TestCase.assertEquals("test_take_hint_1 failed", expected, actual)
    }
}