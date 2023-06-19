package com.eneskayiklik.word_diary.util.mp_chart

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ChartDayFormatter : ValueFormatter() {

    private val formatter = DateTimeFormatter.ofPattern("EEE")

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        /**
         * BarEntry holds data as reversed so if we subtract from 6 we achieve day from today
         * and than we can format
         */
        return LocalDate.now().minusDays((6F - value).toLong())
            .format(formatter)
    }
}