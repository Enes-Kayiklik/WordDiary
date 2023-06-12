package com.eneskayiklik.word_diary.feature.statistics.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.runtime.getValue
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
    modifier: Modifier = Modifier,
    barEntry: List<BarEntry> = emptyList()
) {
    val onSecondaryContainer = MaterialTheme.colorScheme.onSecondaryContainer.toArgb()
    val primary = MaterialTheme.colorScheme.primary.toArgb()
    val secondary = MaterialTheme.colorScheme.inversePrimary.toArgb()

    val barEntries by remember { mutableStateOf(barEntry) }

    Column(modifier = modifier) {
        AndroidView(modifier = Modifier
            .fillMaxWidth()
            .height(400.dp), factory = { context ->
            BarChart(context)
        }, update = { chart ->
            val studiedWordsSet = BarDataSet(barEntries, "").apply {
                colors = listOf(primary, secondary)
                stackLabels = arrayOf("Studied", "New word")
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return if (value != 0F) "%.0f".format(value) else ""
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