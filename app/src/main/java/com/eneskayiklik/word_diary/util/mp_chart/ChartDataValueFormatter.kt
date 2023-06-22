package com.eneskayiklik.word_diary.util.mp_chart

import com.github.mikephil.charting.formatter.ValueFormatter

class ChartDataValueFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return if (value >= 1F) "%.0f".format(value) else ""
    }
}