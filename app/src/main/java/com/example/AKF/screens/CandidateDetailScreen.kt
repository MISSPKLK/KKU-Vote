package com.example.project_election.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.project_election.R
import com.example.project_election.navigation.Routes
import com.example.project_election.models.Candidate
import com.example.project_election.network.RetrofitInstance

@Composable
fun CandidateDetailScreen(
    partyId: Int,
    partyName: String,
    navController: NavController
) {
    var candidates by remember { mutableStateOf<List<Candidate>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(partyId) {
        try {
            candidates = RetrofitInstance.api.getCandidatesByParty(partyId)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    val leadCandidate = candidates.firstOrNull()


    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5FC)).verticalScroll(rememberScrollState())
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(400.dp)) {
            val candidateImage = if (partyId == 1) {
                R.drawable.sorawit_photo
            } else {
                R.drawable.candidate_photo
            }

            Image(
                painter = painterResource(id = candidateImage),
                contentDescription = "Candidate Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)))

            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(30.dp).align(Alignment.TopStart)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }

            Column(modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)) {
                Text(
                    text = leadCandidate?.let { "${it.Party_Fname} ${it.Party_Lname}" } ?: "กำลังโหลดข้อมูล...",
                    color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp
                )
                Text(
                    text = if (partyId == 1) "นายกองค์การนักศึกษา" else "รองนายกองค์การนักศึกษา",
                    color = Color.White, fontSize = 16.sp
                )
            }
        }


        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(text = partyName, fontWeight = FontWeight.Bold, fontSize = 28.sp)
                    Text(text = if(partyId == 2) "Kalapruek" else "Luead See Id", color = Color.Gray, fontSize = 16.sp)
                }
                val logoRes = if (partyId == 2) R.drawable.logo_kalapruek else R.drawable.logo_kku_vote
                Image(painter = painterResource(id = logoRes), contentDescription = "Party Logo", modifier = Modifier.size(60.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "\"การเปลี่ยนแปลงที่ยั่งยืน เริ่มจากพลังของนักศึกษา โดยนักศึกษาและเพื่อเพื่อนนักศึกษาทุกคน\"",
                fontStyle = FontStyle.Italic,
                color = Color.DarkGray,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Card(modifier = Modifier.weight(1f).height(80.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFE2DDF8))) {
                    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        Text("เบอร์", fontSize = 16.sp)
                        Text(partyId.toString(), fontWeight = FontWeight.Bold, fontSize = 28.sp)
                    }
                }
                Card(modifier = Modifier.weight(1f).height(80.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFE2DDF8))) {
                    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        Text("สมาชิกพรรค", fontSize = 16.sp)
                        Text(candidates.size.toString(), fontWeight = FontWeight.Bold, fontSize = 28.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = { navController.navigate(Routes.workExperienceRoute(partyId)) }, // 🌟 แก้ตรงนี้
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA53322))
                ) {
                    Text("ประวัติการทำงาน")
                }
                Button(
                    onClick = { navController.navigate(Routes.partyMembersRoute(partyId, partyName)) }, // 🌟 แก้ตรงนี้
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA53322))
                ) {
                    Text("สมาชิกพรรค")
                }
            }
        }
    }
}