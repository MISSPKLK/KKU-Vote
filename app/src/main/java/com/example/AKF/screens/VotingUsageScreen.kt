package com.example.project_election.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.*

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.project_election.components.VotingPieChart
import com.example.project_election.ui.theme.Purple40
import com.example.project_election.viewmodel.StatsViewModel
import kotlin.compareTo
import kotlin.div
import kotlin.times

@Composable
fun VotingUsageScreen(
    viewModel: StatsViewModel,
    navController: NavHostController
) {
    var selectedYear by remember { mutableStateOf(2569) }
    val stats = viewModel.stats.value

    LaunchedEffect(selectedYear) {
        viewModel.loadStats(selectedYear) // ส่ง พ.ศ. ViewModel แปลงให้
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.popBackStack() }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "ย้อนกลับ",
                    tint = Color(0xFFA53322)
                )
            }
            Text(
                text = "อัตราการใช้สิทธิ์นักศึกษา",
                color = Color(0xFFA53322),
                fontSize = 20.sp,
                style = MaterialTheme.typography.titleMedium
            )
        }

        YearSelector(
            selectedYear = selectedYear,
            onYearChange = { year -> selectedYear = year }
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (stats == null || stats.voters == 0) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "ไม่มีข้อมูล",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
            }
        } else {
            val voted = stats.parties.sumOf { it.votes } + stats.abstain
            val notVoted = stats.students - voted
            val percent = (voted * 100) / stats.students

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.PieChart,
                    contentDescription = "chart icon",
                    tint = Color(0xFFA53322),
                    modifier = Modifier.size(26.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "อัตราการใช้สิทธิ์",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            VotingPieChart(voted = voted, notVoted = notVoted)

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard("ผู้มาใช้สิทธิ", voted, Color(0xFF6A5AE0))
                StatCard("ผู้ไม่มาใช้สิทธิ", notVoted, Color.Gray)
            }

            Spacer(modifier = Modifier.height(16.dp))

            StatCardPercent(percent)

            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}

@Composable
fun StatCard(title: String, value: Int, color: Color) {
    Card(
        modifier = Modifier
            .height(100.dp)
            .width(200.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .background(Color(0xFFF0F0F0), RoundedCornerShape(12.dp))
                    .padding(10.dp)
            ) {
                Text(
                    "$value คน",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
        }
    }
}

@Composable
fun StatCardPercent(percent: Int) {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("อัตราการใช้สิทธิ", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .background(Color(0xFFF0F0F0), RoundedCornerShape(12.dp))
                    .padding(10.dp)
            ) {
                Text(
                    "$percent %",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )
            }
        }
    }
}