package com.eneskayiklik.word_diary

import com.eneskayiklik.word_diary.util.extensions.takeHint
import junit.framework.TestCase
import org.junit.Test

class StringUtilsTest {

    private val fullWord = "converter"

    @Test
    fun test_take_hint_1() {
        val userInput = "Con"
        val expected = "conv"
        val actual = userInput.takeHint(fullWord)

        TestCase.assertEquals("test_take_hint_1 failed", expected, actual)
    }

    @Test
    fun test_take_hint_2() {
        val userInput = "conver"
        val expected = "convert"
        val actual = userInput.takeHint(fullWord)

        TestCase.assertEquals("test_take_hint_2 failed", expected, actual)
    }

    @Test
    fun test_take_hint_3() {
        val userInput = ""
        val expected = "c"
        val actual = userInput.takeHint(fullWord)

        TestCase.assertEquals("test_take_hint_3 failed", expected, actual)
    }

    @Test
    fun test_take_hint_4() {
        val userInput = "converte"
        val expected = "converter"
        val actual = userInput.takeHint(fullWord)

        TestCase.assertEquals("test_take_hint_4 failed", expected, actual)
    }

    @Test
    fun test_take_hint_5() {
        val userInput = "ads"
        val expected = "c"
        val actual = userInput.takeHint(fullWord)

        TestCase.assertEquals("test_take_hint_5 failed", expected, actual)
    }

    @Test
    fun test_take_hint_6() {
        val userInput = "cio"
        val expected = "co"
        val actual = userInput.takeHint(fullWord)

        TestCase.assertEquals("test_take_hint_6 failed", expected, actual)
    }
}