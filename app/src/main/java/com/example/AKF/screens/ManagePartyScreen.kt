package com.example.project_election.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project_election.navigation.Routes
import com.example.project_election.viewmodel.PartyViewModel
import com.example.project_election.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagePartyScreen(
    navController: NavController,
    viewModel: PartyViewModel = viewModel()
) {

    val partyList = viewModel.partyList.value
    val isLoading = viewModel.isLoading.value

    Scaffold(
        containerColor = ElectionBackground,

        // ================= TOP =================
        topBar = {
            Column {
                Spacer(modifier = Modifier.height(40.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = Purple40
                        )
                    }

                    Text(
                        text = "จัดการข้อมูลพรรค",
                        color = Purple40,
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {

            Spacer(modifier = Modifier.height(30.dp))



            Button( onClick = { navController.navigate(Routes.ADD_PARTY) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6F8EFF)),
                modifier = Modifier

                    .fillMaxWidth()
                    .height(100.dp),
                shape = RoundedCornerShape(20.dp) )
            { Column(horizontalAlignment = Alignment.CenterHorizontally)
            {
                Text("+",
                    fontSize = 35.sp,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White)
                Text("เพิ่มพรรค",
                    fontSize = 20.sp,
                    color = Color.White) } }


            Spacer(modifier = Modifier.height(30.dp))

            Text(
                "พรรคที่เพิ่มแล้ว",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            } else {

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    items(partyList) { party ->

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp) // กำหนดความสูงคงที่ให้สมมาตร
                        ) {

                            // ===== การ์ด =====
                            Card(
                                shape = RoundedCornerShape(24.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                border = BorderStroke(1.dp, ElectionCardBorder),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.CenterEnd)
                                    .padding(start = 50.dp)
                            ) {

                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(start = 70.dp, end = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {

                                        Text(
                                            party.Party_name,
                                            fontSize = 16.sp,
                                            color = Color.Black
                                        )

                                        Spacer(modifier = Modifier.height(6.dp))

                                        Row(verticalAlignment = Alignment.CenterVertically) {

                                            Icon(
                                                Icons.Default.Person,
                                                contentDescription = null,
                                                tint = ElectionIconGray,
                                                modifier = Modifier.size(16.dp)
                                            )

                                            Spacer(modifier = Modifier.width(4.dp))

                                            Text(
                                                "สมาชิก ${viewModel.getMemberCount(party.idParties)} คน",
                                                fontSize = 16.sp,
                                                color = ElectionIconGray
                                            )
                                        }
                                    }

                                    // เป็น
                                    IconButton(
                                        onClick = {
                                            navController.navigate("partyDetail/${party.idParties}")
                                        }
                                    ) {
                                        Icon(
                                            Icons.Default.Edit,
                                            contentDescription = null,
                                            tint = ElectionIconGray
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    var showDeleteDialog by remember { mutableStateOf(false) }

                                    IconButton(onClick = { showDeleteDialog = true }) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = null,
                                            tint = ElectionIconGray)
                                    }

                                    if (showDeleteDialog) {
                                        AlertDialog(
                                            onDismissRequest = { showDeleteDialog = false },
                                            title = { Text("ยืนยันการลบ") },
                                            text = { Text("คุณแน่ใจหรือไม่ว่าต้องการลบพรรค \"${party.Party_name}\"?") },
                                            confirmButton = {
                                                TextButton(onClick = {
                                                    viewModel.deleteParty(party.idParties)
                                                    showDeleteDialog = false
                                                }) {
                                                    Text("ลบ", color = Color.Red)
                                                }
                                            },
                                            dismissButton = {
                                                TextButton(onClick = { showDeleteDialog = false }) {
                                                    Text("ยกเลิก")
                                                }
                                            }
                                        )
                                    }
                                }
                            }

                            // ===== กล่องเลข (กึ่งกลางแนวตั้งจริง) =====
                            Box(
                                modifier = Modifier
                                    .height(115.dp)
                                    .width(100.dp)
                                    .align(Alignment.CenterStart) // 👈 กึ่งกลางจริง
                                    .background(
                                        ElectionLightPurple,
                                        RoundedCornerShape(20.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = party.Party_number.toString(),
                                    color = Color.White,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}