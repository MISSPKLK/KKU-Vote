package com.example.project_election.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartyMembersScreen(partyId: Int, partyName: String, navController: NavController) {
    var members by remember { mutableStateOf<List<Candidate>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(partyId) {
        try {
            members = RetrofitInstance.api.getCandidatesByParty(partyId)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("สมาชิกพรรค", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF5F5FC))
            )
        },
        containerColor = Color(0xFFF5F5FC)
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val logoRes = if (partyId == 2) R.drawable.logo_kalapruek else R.drawable.logo_kku_vote
            Image(
                painter = painterResource(id = logoRes),
                contentDescription = "Party Logo",
                modifier = Modifier.size(150.dp).padding(vertical = 16.dp)
            )

            if (isLoading) {
                CircularProgressIndicator()
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(members) { member ->
                        Card(
                            modifier = Modifier.fillMaxWidth().height(100.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize().padding(12.dp)) {
                                Image(
                                    painter = painterResource(id = R.drawable.candidate_photo),
                                    contentDescription = "Profile",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.size(70.dp).clip(RoundedCornerShape(12.dp))
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(text = "${member.Party_Fname} ${member.Party_Lname}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                   Text(text = member.position_name, color = Color.Gray, fontSize = 14.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}