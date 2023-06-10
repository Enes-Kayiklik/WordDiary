package com.eneskayiklik.word_diary.feature.statistics.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.eneskayiklik.word_diary.core.ui.theme.Pink
import com.eneskayiklik.word_diary.core.ui.theme.Purple
import com.eneskayiklik.word_diary.util.mp_chart.ChartDayFormatter
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter

@Composable
fun StatisticsChart(
    modifier: Modifier = Modifier
) {
    val onSecondaryContainer = MaterialTheme.colorScheme.onSecondaryContainer.toArgb()
    val primary = MaterialTheme.colorScheme.primary.toArgb()
    val secondary = MaterialTheme.colorScheme.inversePrimary.toArgb()

    Column(modifier = modifier) {
        AndroidView(modifier = Modifier
            .fillMaxWidth()
            .height(400.dp), factory = { context ->
            BarChart(context)
        }, update = { chart ->
            val studiedWords = listOf(
                BarEntry(0F, arrayOf(12F, 4F).toFloatArray()),
                BarEntry(1F, arrayOf(7F, 5F).toFloatArray()),
                BarEntry(2F, arrayOf(6F, 2F).toFloatArray()),
                BarEntry(3F, arrayOf(10F, 5F).toFloatArray()),
                BarEntry(4F, arrayOf(5F, 7F).toFloatArray()),
                BarEntry(5F, arrayOf(10F, 10F).toFloatArray()),
                BarEntry(6F, arrayOf(20F, 3F).toFloatArray())
            )
            val studiedWordsSet = BarDataSet(studiedWords, "").apply {
                colors = listOf(primary, secondary)
                stackLabels = arrayOf("Studied", "New word")
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "%.0f".format(value)
                    }
                }
                valueTextSize = 12.dp.value
                valueTextColor = Color.White.toArgb()
            }
            val barData = BarData(studiedWordsSet)
            chart.apply {
                data = barData
                setTouchEnabled(false)
                setDrawValueAboveBar(false)
                description.isEnabled = false
                legend.textColor = onSecondaryContainer
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    setDrawAxisLine(true)
                    valueFormatter = ChartDayFormatter()
                    labelRotationAngle = 45F
                    extraBottomOffset = 10F
                    textColor = onSecondaryContainer
                }
                axisRight.apply {
                    isEnabled = false
                    axisMinimum = 0F
                }
                axisLeft.apply {
                    axisMinimum = 0F
                    textColor = onSecondaryContainer
                }
                setFitBars(true)
                invalidate()
                animateY(500, Easing.EaseInOutCirc)
            }
        })
    }
}