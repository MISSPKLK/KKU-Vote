package com.example.project_election.screens

import android.R.attr.data
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.project_election.components.AppBottomBar

import androidx.compose.foundation.Image
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.res.painterResource
import com.example.project_election.R
import androidx.compose.ui.layout.ContentScale
import com.example.project_election.models.PartyVote
import com.example.projectmode.ElectionViewModel

import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter



@Composable
fun ResultScreen(
    viewModel: ElectionViewModel,
    navController: NavController,
    currentRoute: String?
) {

    val result by viewModel.result.collectAsState()
    val status by viewModel.status.collectAsState()



    val formattedTime = status?.endTime?.let {
        try {
            val dateTime = OffsetDateTime.parse(it)
            dateTime
                .atZoneSameInstant(ZoneId.of("Asia/Bangkok"))
                .format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm"))
        } catch (e: Exception) {
            it
        }
    } ?: ""

    LaunchedEffect(Unit) {
        viewModel.fetchResult()
        viewModel.fetchStatus()
    }

    Scaffold(
        bottomBar = {
            AppBottomBar(
                navController = navController,
                currentRoute = currentRoute
            )
        }
    ) { padding ->

        if (result == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF6A5AE0))
            }
            return@Scaffold
        }

        val data = result!!

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFEDE7F6), Color.White)
                    )
                )
                .padding(16.dp)
                .verticalScroll(rememberScrollState())   // 👈 เพิ่มบรรทัดนี้
                .padding(16.dp)
        ) {

            // ================= HEADER =================

            Text(
                "📊 ผลการเลือกตั้ง",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A3F9A)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ================= STATUS CARD =================

            status?.let { electionStatus ->

                val isClosed = electionStatus.status == "CLOSED"

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isClosed)
                            Color(0xFFE57373)
                        else
                            Color(0xFF66BB6A)
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {

                        Text(
                            if (isClosed)
                                "🔒 ปิดการเลือกตั้งแล้ว"
                            else
                                "🟢 กำลังเปิดให้ลงคะแนน",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        status?.let { s ->



                            Text(
                                "สิ้นสุด: $formattedTime",
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // ================= STAT CARDS =================

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatCard("ผู้มีสิทธิ์", data.totalEligible.toString(), Color(0xFF42A5F5))   // ฟ้ากลาง
                StatCard("มาใช้สิทธิ์", data.totalVoted.toString(), Color(0xFFAB47BC))      // ม่วงกลาง
                StatCard("ไม่ประสงค์", data.abstain.toString(), Color(0xFFBDBDBD))          // เทากลาง
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ================= WINNER CARD =================

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFB39DDB)
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        "🏆 พรรคที่ชนะ",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    val winnerLogo = when (data.winner) {
                        "กาลพฤกษ์" -> R.drawable.banner_kalapruek
                        "เลือดสีอิฐ" -> R.drawable.banner_luead_see_id_group
                        else -> R.drawable.ic_launcher_foreground
                    }

                    Image(
                        painter = painterResource(id = winnerLogo),
                        contentDescription = "Winner",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        data.winner,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    val winnerVotes = data.partyVotes
                        .find { it.party_name == data.winner }
                        ?.total ?: 0

                    Text(
                        "$winnerVotes คะแนน",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ================= PARTY LIST =================

            Text(
                "คะแนนแต่ละพรรค",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A3F9A)
            )

            Spacer(modifier = Modifier.height(12.dp))

            val sortedParties = data.partyVotes
                .sortedWith(
                    compareByDescending<PartyVote> { it.party_name == data.winner }
                        .thenByDescending { it.total }
                )
            val totalPartyVotes = data.partyVotes.sumOf { it.total }

            sortedParties.forEach { party ->

                val percent = if (totalPartyVotes > 0)
                    (party.total * 100f / totalPartyVotes)
                else 0f

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF3F0FF)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            party.party_name,
                            fontWeight = FontWeight.Medium
                        )

                        Text(
                            "${party.total} คะแนน (${String.format("%.1f", percent)}%)",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6A5AE0)
                        )
                    }
                }
            }
            // ================= TIME DISTRIBUTION =================

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "สัดส่วนช่วงเวลาการลงคะแนน",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A3F9A)
            )

            Spacer(modifier = Modifier.height(16.dp))

            val morning = data.timeStats?.morning ?: 0
            val afternoon = data.timeStats?.afternoon ?: 0
            val evening = data.timeStats?.evening ?: 0

            val totalTimeVotes = (morning + afternoon + evening).takeIf { it > 0 } ?: 1

            TimeBar("🌅 เช้า", morning, totalTimeVotes)
            TimeBar("🌤 บ่าย", afternoon, totalTimeVotes)
            TimeBar("🌆 เย็น", evening, totalTimeVotes)
        }
    }
}


@Composable
fun StatCard(title: String, value: String, color: Color) {
    Card(
        modifier = Modifier
            .width(110.dp)
            .height(90.dp),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = color
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                title,
                fontSize = 12.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun TimeBar(title: String, value: Int, total: Int) {

    val percentage = value.toFloat() / total.toFloat()

    Column(modifier = Modifier.padding(vertical = 6.dp)) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title)
            Text("$value คน")
        }

        Spacer(modifier = Modifier.height(4.dp))

        LinearProgressIndicator(
            progress = percentage,
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(8.dp)),
            color = Color(0xFF6A5AE0),
            trackColor = Color(0xFFEDE7F6)
        )
    }
}