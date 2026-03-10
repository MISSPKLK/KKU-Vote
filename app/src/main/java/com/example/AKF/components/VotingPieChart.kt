package com.example.project_election.components

import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

@Composable
fun VotingPieChart(
    voted: Int,
    notVoted: Int
) {

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),

        factory = { context ->
            PieChart(context)
        },

        update = { chart ->

            val entries = listOf(
                PieEntry(voted.toFloat(), "นักศึกษาที่มาใช้สิทธิ"),
                PieEntry(notVoted.toFloat(), "นักศึกษาที่ไม่มาใช้สิทธิ")
            )

            val dataSet = PieDataSet(entries, "")

            dataSet.colors = listOf(
                Color.parseColor("#6A5AE0"),
                Color.LTGRAY
            )
            dataSet.valueTextSize = 14f

            val data = PieData(dataSet)
            chart.setUsePercentValues(true)
            data.setValueFormatter(
                com.github.mikephil.charting.formatter.PercentFormatter(chart)
            )

            chart.data = data
            chart.description.isEnabled = false
            chart.setDrawEntryLabels(false)
            chart.setUsePercentValues(true)
            chart.legend.textSize = 14f

            chart.invalidate()
        }
    )
}