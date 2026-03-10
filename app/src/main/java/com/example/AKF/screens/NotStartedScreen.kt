package com.example.project_election.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.project_election.R
import com.example.project_election.components.AppBottomBar
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.OffsetDateTime
import java.time.ZoneId

@Composable
fun NotStartedScreen(
    startTime: String?,
    navController: NavController,
    currentRoute: String?,
    onStart: () -> Unit
) {
    var hours by remember { mutableStateOf("00") }
    var minutes by remember { mutableStateOf("00") }
    var seconds by remember { mutableStateOf("00") }
    var canStart by remember { mutableStateOf(false) }

    LaunchedEffect(startTime) {
        if (startTime == null) return@LaunchedEffect

        while (true) {
            val start = OffsetDateTime.parse(startTime)
                .atZoneSameInstant(ZoneId.of("Asia/Bangkok"))
                .toInstant()

            val now = java.time.Instant.now()
            val duration = Duration.between(now, start)

            if (!duration.isNegative && !duration.isZero) {
                hours = String.format("%02d", duration.toHours())
                minutes = String.format("%02d", duration.toMinutes() % 60)
                seconds = String.format("%02d", duration.seconds % 60)
                canStart = false
            } else {
                hours = "00"
                minutes = "00"
                seconds = "00"
                canStart = true
                break
            }
            delay(1000)
        }
    }

    Scaffold(
        bottomBar = { AppBottomBar(navController, currentRoute) },
        containerColor = Color(0xFFF5F5FC)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))


            Image(
                painter = painterResource(id = R.drawable.logo_kku_vote),
                contentDescription = "Election Waiting",
                modifier = Modifier
                    .size(140.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .padding(16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "เตรียมพร้อมใช้สิทธิ์ของคุณ",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "การเลือกตั้งจะเริ่มเปิดหีบในอีก",
                fontSize = 15.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                TimeBox(value = hours, label = "ชั่วโมง")
                Text(text = ":", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFFA53322), modifier = Modifier.padding(horizontal = 8.dp).offset(y = (-12).dp))
                TimeBox(value = minutes, label = "นาที")
                Text(text = ":", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFFA53322), modifier = Modifier.padding(horizontal = 8.dp).offset(y = (-12).dp))
                TimeBox(value = seconds, label = "วินาที")
            }

            Spacer(modifier = Modifier.height(32.dp))


            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFFA53322), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "ข้อควรรู้ก่อนลงคะแนน", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    GuidelineRow(text = "นักศึกษา 1 คน สามารถลงคะแนนได้เพียง 1 ครั้งเท่านั้น")
                    Spacer(modifier = Modifier.height(8.dp))
                    GuidelineRow(text = "หากกดยืนยันแล้ว จะไม่สามารถแก้ไขคะแนนได้")
                    Spacer(modifier = Modifier.height(8.dp))
                    GuidelineRow(text = "สามารถเลือก 'ไม่ประสงค์ลงคะแนน' ได้")
                }
            }

            Spacer(modifier = Modifier.weight(1f))


            Button(
                onClick = { if (canStart) onStart() },
                enabled = canStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFA53322),
                    disabledContainerColor = Color.LightGray
                )
            ) {
                Text(
                    text = if (canStart) "🗳️ ไปหน้าลงคะแนน" else "🔒 กรุณารอเวลาเปิดหีบ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (canStart) Color.White else Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun TimeBox(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .background(Color.White, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFA53322)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, fontSize = 14.sp, color = Color.Gray, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun GuidelineRow(text: String) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(Icons.Default.CheckCircleOutline, contentDescription = null, tint = Color(0xFF4CAF50), modifier = Modifier.size(18.dp).offset(y = 2.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
    }
}