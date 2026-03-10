package com.example.project_election.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.project_election.R
import com.example.project_election.components.AppBottomBar
import com.example.project_election.navigation.Routes
import com.example.projectmode.ElectionViewModel
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.OffsetDateTime
import java.time.ZoneId

@Composable
fun CountdownScreen(
    endTime: String,
    viewModel: ElectionViewModel,
    winnerName: String? = null,
    winnerImageUrl: String? = null,
    navController: NavController,
    onTimeUp: () -> Unit,
    currentRoute: String?
) {
    var hours by remember { mutableStateOf("00") }
    var minutes by remember { mutableStateOf("00") }
    var seconds by remember { mutableStateOf("00") }

    // ดึงสถานะว่าโหวตไปหรือยังจาก ViewModel
    val hasVoted by viewModel.hasVoted.collectAsState()

    LaunchedEffect(endTime) {
        val end = OffsetDateTime.parse(endTime)
            .atZoneSameInstant(ZoneId.of("Asia/Bangkok"))
            .toInstant()

        while (true) {
            val now = java.time.Instant.now()
            val duration = Duration.between(now, end)

            if (!duration.isNegative && !duration.isZero) {
                hours = String.format("%02d", duration.toHours())
                minutes = String.format("%02d", duration.toMinutes() % 60)
                seconds = String.format("%02d", duration.seconds % 60)
            } else {
                hours = "00"
                minutes = "00"
                seconds = "00"
                onTimeUp()
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

            // ส่วนหัว (Header)
            Box(
                modifier = Modifier
                    .background(Color(0xFFE8F5E9), RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "🟢 ระบบกำลังเปิดรับการลงคะแนน",
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "เวลาที่เหลือในการลงคะแนน",
                fontSize = 18.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // นาฬิกานับถอยหลังแบบ Digital
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                DigitalTimeBox(value = hours, label = "ชั่วโมง")
                Text(text = ":", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color(0xFFA53322), modifier = Modifier.padding(horizontal = 8.dp).offset(y = (-12).dp))
                DigitalTimeBox(value = minutes, label = "นาที")
                Text(text = ":", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color(0xFFA53322), modifier = Modifier.padding(horizontal = 8.dp).offset(y = (-12).dp))
                DigitalTimeBox(value = seconds, label = "วินาที")
            }

            Spacer(modifier = Modifier.height(40.dp))

            // รูปภาพตรงกลางเพื่อลดพื้นที่ว่าง
            Image(
                painter = painterResource(id = R.drawable.logo_kku_vote),
                contentDescription = "Voting Graphic",
                modifier = Modifier
                    .size(160.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.weight(1f))

            // เช็คสถานะการโหวต เพื่อแสดงปุ่มหรือข้อความ
            if (hasVoted) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF4CAF50), modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "คุณได้ใช้สิทธิ์ลงคะแนนเรียบร้อยแล้ว\nขอบคุณที่ร่วมเป็นส่วนหนึ่งในการเลือกตั้ง",
                            color = Color(0xFF2E7D32),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                Button(
                    onClick = {
                        val encoded = java.net.URLEncoder.encode(endTime, "UTF-8")
                        navController.navigate("voting/$encoded")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA53322)),
                    elevation = ButtonDefaults.buttonElevation(4.dp)
                ) {
                    Icon(Icons.Default.HowToVote, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "เข้าสู่คูหาลงคะแนน",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun DigitalTimeBox(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier.size(75.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = value,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFA53322)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, fontSize = 14.sp, color = Color.Gray, fontWeight = FontWeight.SemiBold)
    }
}