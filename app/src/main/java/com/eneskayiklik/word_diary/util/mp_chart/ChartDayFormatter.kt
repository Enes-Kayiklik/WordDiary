package com.eneskayiklik.word_diary.util.mp_chart

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.Locale

class ChartDayFormatter : ValueFormatter() {

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return SimpleDateFormat(
            "dd MMMM",
            Locale.ROOT
        ).format(System.currentTimeMillis() - (((axis?.labelCount ?: 7) - 1) - value.toInt()) * 86_400_000)
    }
}