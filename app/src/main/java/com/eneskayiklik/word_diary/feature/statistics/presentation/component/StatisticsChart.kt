package com.eneskayiklik.word_diary.feature.statistics.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.util.mp_chart.ChartDataValueFormatter
import com.eneskayiklik.word_diary.util.mp_chart.ChartDayFormatter
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

@Composable
fun StatisticsChart(
    modifier: Modifier = Modifier,
    studiedBarEntry: List<BarEntry> = emptyList(),
    newWordBarEntry: List<BarEntry> = emptyList(),
) {
    val onSecondaryContainer = MaterialTheme.colorScheme.onSecondaryContainer.toArgb()
    val primary = MaterialTheme.colorScheme.primary.toArgb()
    val secondary = MaterialTheme.colorScheme.inversePrimary.toArgb()

    val studiedTitle = stringResource(id = R.string.chart_studied)
    val newWordTitle = stringResource(id = R.string.chart_new_word)

    Column(modifier = modifier) {
        AndroidView(modifier = Modifier
            .fillMaxWidth()
            .height(400.dp),
            factory = { context ->
                BarChart(context).let { chart ->
                    val studiedWordsSet = BarDataSet(studiedBarEntry, studiedTitle).apply {
                        color = primary
                        valueFormatter = ChartDataValueFormatter()
                        valueTextSize = 12.dp.value
                        valueTextColor = onSecondaryContainer
                    }
                    val newWordSet = BarDataSet(newWordBarEntry, newWordTitle).apply {
                        color = secondary
                        valueFormatter = ChartDataValueFormatter()
                        valueTextSize = 12.dp.value
                        valueTextColor = onSecondaryContainer
                    }

                    val barData = BarData(studiedWordsSet, newWordSet)
                    chart.apply {
                        data = barData
                        setTouchEnabled(false)
                        description.isEnabled = false
                        legend.textColor = onSecondaryContainer
                        setFitBars(true)
                        /*legend.apply {
                            orientation = Legend.LegendOrientation.VERTICAL
                            setDrawInside(true)
                            verticalAlignment = Legend.LegendVerticalAlignment.TOP
                        }*/
                        xAxis.apply {
                            position = XAxis.XAxisPosition.BOTTOM
                            setDrawGridLines(false)
                            setDrawAxisLine(false)
                            valueFormatter = ChartDayFormatter()
                            extraBottomOffset = 8.dp.value
                            textColor = onSecondaryContainer
                            setCenterAxisLabels(true)
                            axisMinimum = 0F
                            axisMaximum = 7F
                        }
                        axisRight.apply {
                            isEnabled = false
                            axisMinimum = 0F
                        }
                        axisLeft.apply {
                            isEnabled = false
                            axisMinimum = 0F
                        }
                        data.barWidth = .40F
                        groupBars(0F, .1F, .05F)
                        invalidate()
                    }
                }
            }
        )
    }
}