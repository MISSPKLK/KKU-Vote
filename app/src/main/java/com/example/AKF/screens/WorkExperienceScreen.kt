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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.project_election.R
import com.example.project_election.models.Candidate
import com.example.project_election.network.RetrofitInstance

@Composable
fun WorkExperienceScreen(partyId: Int, navController: NavController) {
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

    val candidate = candidates.firstOrNull()

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5FC)).verticalScroll(rememberScrollState())
    ) {

        Box(modifier = Modifier.fillMaxWidth().height(400.dp)) {

            val headerImage = if (partyId == 1) R.drawable.sorawit_photo else R.drawable.candidate_photo

            Image(
                painter = painterResource(id = headerImage),
                contentDescription = "Candidate Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)))

            IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.padding(30.dp)) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }

            Column(modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)) {
                Text(
                    text = candidate?.let { "${it.Party_Fname} ${it.Party_Lname}" } ?: "ไม่พบข้อมูล",
                    color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp
                )
                Text(text = "ประวัติการทำงาน", color = Color.White, fontSize = 16.sp)
            }
        }

        Column(modifier = Modifier.padding(24.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Working Experience", fontWeight = FontWeight.Bold, fontSize = 26.sp)
                val logoRes = if (partyId == 1) R.drawable.logo_lsi else R.drawable.logo_kalapruek
                Image(painter = painterResource(id = logoRes), contentDescription = "Logo", modifier = Modifier.size(60.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {

                val experiences = candidate?.biography?.split("\n\n") ?: listOf("ไม่มีข้อมูล")

                experiences.forEach { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Text(
                            text = item,
                            modifier = Modifier.padding(20.dp),
                            fontSize = 15.sp,
                            lineHeight = 22.sp
                        )
                    }
                }
            }
        }
    }
}