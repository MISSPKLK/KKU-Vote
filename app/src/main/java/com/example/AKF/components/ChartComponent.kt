package com.example.project_election.components


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.project_election.models.Party

import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import kotlin.collections.forEach
import kotlin.collections.toList

//@Composable
//fun ChartComponent(
//    parties: List<Party>,
//    abstain: Float
//) {
//
//    AndroidView(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(250.dp),
//        factory = { context ->
//
//            PieChart(context).apply {
//
//                val entries = mutableListOf<PieEntry>()
//                setDrawEntryLabels(false)
//
//                parties.forEach {
//                    entries.add(
//                        PieEntry(it.votes.toFloat(), it.Party_name)
//                    )
//                }
//
//                entries.add(PieEntry(abstain, "ไม่ประสงค์"))
//
//
//
//                val dataSet = PieDataSet(entries, "")
//
//                dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
//                dataSet.valueTextSize = 14f
//
//                val data = PieData(dataSet)
//
//                setUsePercentValues(true)
//                data.setValueFormatter(
//                    com.github.mikephil.charting.formatter.PercentFormatter(this)
//                )
//
//                this.data = data
//                description.isEnabled = false
//
//                animateY(1200)
//
//                invalidate()
//            }
//
//
//        }
//    )
//
//}
@Composable
fun ChartComponent(
    parties: List<Party>,
    abstain: Float
) {

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),

        factory = { context ->
            PieChart(context)
        },

        update = { chart ->

            val entries = mutableListOf<PieEntry>()

            parties.forEach {
                entries.add(
                    PieEntry(it.votes.toFloat(), it.Party_name)
                )
            }

            entries.add(PieEntry(abstain, "ไม่ประสงค์"))

            val dataSet = PieDataSet(entries, "")
            dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
            dataSet.valueTextSize = 14f

            val data = PieData(dataSet)

            chart.setUsePercentValues(true)
            data.setValueFormatter(
                com.github.mikephil.charting.formatter.PercentFormatter(chart)
            )

            chart.data = data
            chart.setDrawEntryLabels(false)
            chart.description.isEnabled = false

            chart.legend.textSize = 14f

            chart.invalidate() // refresh
        }
    )
}