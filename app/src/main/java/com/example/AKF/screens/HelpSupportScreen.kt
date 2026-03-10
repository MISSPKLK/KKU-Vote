package com.example.project_election.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpSupportScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ช่วยเหลือและสนับสนุน", fontWeight = FontWeight.Bold, color = Color(0xFFA53322)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFFA53322))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF5F5FC))
            )
        },
        containerColor = Color(0xFFF5F5FC)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("คำถามที่พบบ่อย (FAQ)", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(12.dp))

            FaqItem(question = "ไม่สามารถเข้าสู่ระบบได้?", answer = "โปรดตรวจสอบว่าคุณใช้อีเมลของมหาวิทยาลัยขอนแก่น (@kku.ac.th) ในการเข้าสู่ระบบเท่านั้น")
            Spacer(modifier = Modifier.height(8.dp))
            FaqItem(question = "แก้ไขคะแนนโหวตได้หรือไม่?", answer = "ไม่สามารถแก้ไขได้ การลงคะแนนโหวตสามารถทำได้เพียง 1 ครั้งเท่านั้น และไม่สามารถเปลี่ยนแปลงได้")

            Spacer(modifier = Modifier.height(32.dp))
            Text("ติดต่อเจ้าหน้าที่", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(12.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Phone, contentDescription = null, tint = Color(0xFFA53322))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("043-202-660 (กองกิจการนักศึกษา)", fontSize = 15.sp)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Email, contentDescription = null, tint = Color(0xFFA53322))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("support_election@kku.ac.th", fontSize = 15.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun FaqItem(question: String, answer: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = question, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFFA53322))
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = answer, fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
        }
    }
}