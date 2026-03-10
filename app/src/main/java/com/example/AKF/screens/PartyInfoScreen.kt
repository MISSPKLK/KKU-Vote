package com.example.projectmode

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.layout.ContentScale
import com.example.project_election.viewmodel.PartyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartyInfoScreen(
    navController: NavController,
    partyId: String,
    viewModel: PartyViewModel = viewModel()
) {

    LaunchedEffect(partyId) {
        viewModel.loadPartyDetail(partyId)
        viewModel.loadPoliciesByParty(partyId)
    }

    val party = viewModel.selectedParty
    val policies = viewModel.policyList

    Scaffold(
        containerColor = Color(0xFFF5F5F5),

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "รายละเอียดพรรค",
                        color = PrimaryPurple,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "ย้อนกลับ",
                            tint = PrimaryPurple
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { padding ->

        party?.let { p ->

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    top = 16.dp,
                    bottom = 100.dp   // ⭐ เผื่อพื้นที่ปุ่มล่าง
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // ===== HEADER =====
                item {

                    if (!p.image_poster.isNullOrEmpty()) {
                        AsyncImage(
                            model = "http://10.0.2.2:3000/uploads/${p.image_poster}",
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                        Spacer(Modifier.height(16.dp))
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        // LOGO
                        AsyncImage(
                            model = "http://10.0.2.2:3000/uploads/${p.logo}",
                            contentDescription = null,
                            contentScale = ContentScale.Crop,   // ⭐ ทำให้ภาพเต็มวง
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {

                            Text(
                                text = "${p.Party_PrefixName ?: ""} ${p.Party_name}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(Modifier.height(4.dp))

                            Text(
                                text = "หมายเลขพรรค ${p.Party_number}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryPurple
                            )

                            Spacer(Modifier.height(4.dp))

                            Text(
                                text = "สมาชิก ${p.member_count} คน",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }

                }

                // ===== DESCRIPTION =====
                item {

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = "คำอธิบายพรรค",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = p.Party_description ?: "-",
                        fontSize = 15.sp,
                        lineHeight = 22.sp
                    )
                }

                // ===== POLICIES =====
                item {

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = "นโยบายพรรค",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    Spacer(Modifier.height(8.dp))
                }

                itemsIndexed(policies) { index, policy ->

                    Column {

                        Text(
                            text = "${index + 1}. ${policy.policy_title}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = policy.policy_description,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            color = Color.DarkGray
                        )

                        Spacer(Modifier.height(12.dp))
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {


                    Button(
                        onClick = { /* TODO: แก้ไขพรรค */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                    ) {
                        Icon(Icons.Default.Edit, null)
                        Spacer(Modifier.width(6.dp))
                        Text("แก้ไข", fontSize = 18.sp)
                    }

                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }
}