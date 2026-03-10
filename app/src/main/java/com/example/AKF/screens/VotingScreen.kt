package com.example.project_election.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project_election.models.Party
import com.example.project_election.navigation.Routes.HOME

import com.example.projectmode.ElectionViewModel
import kotlinx.coroutines.delay
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VotingScreen(
    navController: NavController,
    currentRoute: String?,
    endTime: String,
    viewModel: ElectionViewModel = viewModel()
) {
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showResultDialog by remember { mutableStateOf(false) }

    val isSuccess by viewModel.isSuccess.collectAsState()
    val statusMessage by viewModel.statusMessage.collectAsState()

    var selectedParty by remember { mutableStateOf<Party?>(null) }
    val status by viewModel.status.collectAsState()

    var prevMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(statusMessage) {
        if (statusMessage != null && statusMessage != prevMessage) {
            prevMessage = statusMessage
            showResultDialog = true
        }
    }

    val partyOptions = listOf(
        Party(
            idParties = 1,
            Party_number = 1,
            Party_PrefixName = null,
            Party_name = "เลือดสีอิฐ",
            Party_description = "-",
            logo = "logo_lsi",
            image_poster = null
        ),
        Party(
            idParties = 2,
            Party_number = 2,
            Party_PrefixName = null,
            Party_name = "กาลพฤกษ์",
            Party_description = "-",
            logo = "logo_kalapruek",
            image_poster = null
        ),
        Party(
            idParties = 3,
            Party_number = 3,
            Party_PrefixName = null,
            Party_name = "ไม่ประสงค์ลงคะแนน",
            Party_description = "-",
            logo = "",
            image_poster = null
        )
    )


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("โหวต", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF5F5FC))
            )
        },
        bottomBar = { AppBottomBar(navController, currentRoute) },
        containerColor = Color(0xFFF5F5FC)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()

                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {

            Text(
                text = "เลือกตั้งสภานักศึกษาและองค์การนักศึกษา มหาวิทยาลัยขอนแก่น",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(start = 20.dp, top = 16.dp, bottom = 6.dp)
            )

            CountdownTimer(endTime = endTime)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                partyOptions.forEach { party ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        elevation = CardDefaults.cardElevation(6.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFAEABFF)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = (selectedParty?.idParties == party.idParties),
                                    onClick = { selectedParty = party }
                                )
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (selectedParty?.idParties == party.idParties),
                                onClick = { selectedParty = party }
                            )

                            val imageRes = when (party.Party_number) {
                                1 -> R.drawable.banner_luead_see_id_group
                                2 -> R.drawable.banner_kalapruek
                                else -> null
                            }

                            if (imageRes != null) {
                                Image(
                                    painter = painterResource(imageRes),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(150.dp)
                                        .clip(RoundedCornerShape(20.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Text(
                                text = party.Party_name,
                                color = Color.White,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(start = 12.dp)
                            )
                        }
                    }
                }
                Button(
                    onClick = { showConfirmDialog = true },
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    enabled = selectedParty != null
                ) {
                    Text("ยืนยันการลงคะแนน")
                }
            }
        }
    }
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("ยืนยันการลงคะแนน") },
            text = { Text("คุณต้องการลงคะแนนให้ \"${selectedParty?.Party_name}\" ใช่หรือไม่?") },
            confirmButton = {
                Button(onClick = {
                    showConfirmDialog = false
                    selectedParty?.let { party ->
                        viewModel.submitVote(
                            electionId = status?.id ?: 1,
                            partyId = party.idParties,
                            isAbstain = party.idParties == 3
                        )
                    }
                }) {
                    Text("ยืนยัน")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showConfirmDialog = false }) {
                    Text("ยกเลิก")
                }
            }
        )
    }

    if (showResultDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(if (isSuccess) "ลงคะแนนสำเร็จ"
                else "")
            },
            text = {
                Text(statusMessage ?: "")
            },
            confirmButton = {
                Button(onClick = {
                    showResultDialog = false
                    if (isSuccess) navController.navigate(route = HOME)
                }) {
                    Text(if (isSuccess) "กลับหน้าหลัก" else "ตกลง")
                }
            }
        )
    }
}

@Composable
fun CountdownTimer(endTime: String) {
    var remainingTime by remember { mutableStateOf("") }

    LaunchedEffect(endTime) {
        val end = Instant.parse(endTime).toEpochMilli()
        while (true) {
            val diff = end - System.currentTimeMillis()
            if (diff <= 0) {
                remainingTime = "หมดเวลาแล้ว"
                break
            }
            val hours = diff / (1000 * 60 * 60)
            val minutes = (diff / (1000 * 60)) % 60
            val seconds = (diff / 1000) % 60
            remainingTime = "$hours : $minutes : $seconds "
            delay(1000)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("เหลือเวลาลงคะแนน", fontSize = 14.sp, color = Color.Gray)
        Text(
            text = remainingTime,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
    }
}