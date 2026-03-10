package com.example.project_election.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.project_election.RetrofitClient
import com.example.project_election.components.ChartComponent
import com.example.project_election.components.PartyResultCard
import com.example.project_election.models.ElectionStats
import com.example.project_election.ui.theme.Purple40
import kotlin.collections.forEach



@Composable
fun ElectionStatsScreen(stats: ElectionStats,
                        onOpenVotingUsage: (ElectionStats) -> Unit,
                        navController: NavHostController
){

    var selectedYear by remember { mutableStateOf(2569) }
    var statsData by remember { mutableStateOf<ElectionStats?>(stats) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        try {
            val yearCE = selectedYear - 543
            val response = RetrofitClient.api.getStatsByYear(yearCE)
            statsData = response
        } catch (e: Exception) {
            statsData = null
        }
    }
    statsData?.let { stats ->


    Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE07C77))
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
            ){
        // ⭐ เพิ่มแค่นี้
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
                text = "สถิติการลงคะแนน",
                color = Color(0xFFA53322),
                fontSize = 20.sp,
                style = MaterialTheme.typography.titleMedium
            )
        }


        YearSelector(
            selectedYear = selectedYear,
            onYearChange = { year ->

                selectedYear = year



                scope.launch {
                    try {
                        val yearCE = year - 543  // พ.ศ. → ค.ศ.
                        val response = RetrofitClient.api.getStatsByYear(yearCE)
                        statsData = response
                    } catch (e: Exception) {
                        statsData = null
                    }
                }
            }
        )
        if (statsData == null || statsData!!.voters == 0) {

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

            Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color(0xFFA53322),
                    RoundedCornerShape(12.dp)
                )
                .padding(12.dp)
        ) {

            Text(
                text = "ผลการเลือกตั้งสภานักศึกษา $selectedYear",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row {
                stats.parties.forEach {



                    AsyncImage(
                        model = "http://10.0.2.2:3000/uploads/${it.logo}",
                        contentDescription = it.Party_name,
                        modifier = Modifier
                            .size(70.dp)
                            .background(
                                Color.White,
                                shape = CircleShape
                            )
                            .padding(3.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.width(6.dp))
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.People,
                contentDescription = "chart icon",
                tint = Color(0xFFA53322),
                modifier = Modifier.size(26.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "จำนวนนักศึกษาที่มาใช้สิทธิ",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

//        Text("ผู้ใช้สิทธิ์ ${stats.voters} จาก ${stats.students}")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier
                    .shadow(4.dp, RoundedCornerShape(12.dp))
                    .background(
                        Color.White,
                        RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "${stats.voters} จาก ${stats.students} คน",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.PieChart,
                contentDescription = "chart icon",
                tint = Color(0xFFA53322),
                modifier = Modifier.size(26.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "อัตราคะแนนเสียง",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        ChartComponent(
            parties = stats.parties,
            abstain = stats.abstain.toFloat()
        )
            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    onOpenVotingUsage(stats)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("ดูอัตราการใช้สิทธิ์นักศึกษา")
            }

        stats.parties.forEach {

            val percent =
                (it.votes.toFloat() / stats.voters.toFloat()) * 100


            PartyResultCard(
                name = it.Party_name,
                votes = it.votes,
                percent = percent.toInt(),
                image_poster = it.image_poster ?: ""

            )
        }
    } ?: run {

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
    }
    }
}}

@Composable
fun YearSelector(
    selectedYear: Int,
    onYearChange: (Int) -> Unit
) {

    val years = listOf(2569, 2568, 2567, 2566)


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0xFFB8B6E8),
                RoundedCornerShape(50)
            )
            .padding(6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        years.forEach { year ->

            val isSelected = year == selectedYear



            Text(
                text = year.toString(),
                modifier = Modifier
                    .weight(1f)
                    .clickable { onYearChange(year) }
                    .background(
                        if (isSelected) Color(0xFF6C6ACF) else Color.Transparent,
                        RoundedCornerShape(50)
                    )
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                color = if (isSelected) Color.White else Color.DarkGray,
                fontSize = 16.sp
            )
        }
    }
}