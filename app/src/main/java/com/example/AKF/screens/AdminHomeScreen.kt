package com.example.projectmode

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project_election.navigation.Routes
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

val LightPurpleBackground = Color.White
val PrimaryPurple = Color(0xFF7E79D6)
val LogoutRed = Color(0xFFF0625D)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AdminHomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val electionViewModel: ElectionViewModel = viewModel()
    var showLogoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        electionViewModel.loadCurrentElection()
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("ออกจากระบบ") },
            text = { Text("คุณต้องการออกจากระบบใช่หรือไม่?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                ) { Text("ใช่") }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("ยกเลิก") }
            }
        )
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LightPurpleBackground)
                .padding(paddingValues),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                ProfileSection(email = "admin@kku.ac.th")
                Spacer(modifier = Modifier.height(32.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F3FF)),
                    elevation = CardDefaults.cardElevation(6.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))

                        val election = electionViewModel.currentElection
                        val hasElection = election != null

                        if (election != null) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 12.dp),
                                    shape = RoundedCornerShape(36.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    elevation = CardDefaults.cardElevation(2.dp),
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(20.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                    ) {
                                        Text(
                                            text = election.election_name,
                                            color = PrimaryPurple,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            textAlign = TextAlign.Center,
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = formatThaiDateTimeRange(election.start_time, election.end_time),
                                            fontSize = 14.sp,
                                            color = PrimaryPurple.copy(alpha = 0.8f),
                                            fontWeight = FontWeight.Medium,
                                            textAlign = TextAlign.Center,
                                        )
                                    }
                                }
                                IconButton(
                                    onClick = { navController.navigate("editElection/${election.idElections}") },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .offset(x = 2.dp, y = 8.dp)
                                        .size(36.dp)
                                        .background(Color.White, CircleShape)
                                        .border(1.5.dp, PrimaryPurple.copy(alpha = 0.4f), CircleShape),
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = null, tint = PrimaryPurple)
                                }
                            }
                        } else {
                            MenuButtonLong(
                                title = "จัดการการเลือกตั้ง",
                                icon = Icons.Default.Add,
                                onClick = { navController.navigate("manage_election") },
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            MenuButtonSquare(
                                title = "จัดการข้อมูลพรรค",
                                icon = Icons.Default.Groups,
                                modifier = Modifier.weight(1f),
                                enabled = hasElection,
                                onClick = { navController.navigate(Routes.MANAGE_PARTY) },
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            MenuButtonSquare(
                                title = "ตรวจสอบสถิติ",
                                icon = Icons.Default.Search,
                                modifier = Modifier.weight(1f),
                                enabled = true,
                                onClick = { navController.navigate("stats") },
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)
                        Spacer(modifier = Modifier.height(24.dp))

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { navController.navigate("send_notification") },
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(2.dp),
                        ) {
                            Row(
                                modifier = Modifier.padding(20.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(Color(0xFFFDECEA), CircleShape),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Icon(
                                        Icons.Default.NotificationsActive,
                                        contentDescription = null,
                                        tint = Color(0xFFA53322),
                                    )
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Text("ยิงแจ้งเตือนประกาศ", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Spacer(modifier = Modifier.weight(1f))
                                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = { showLogoutDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = LogoutRed),
                            shape = RoundedCornerShape(26.dp),
                        ) {
                            Text("ออกจากระบบ", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileSection(email: String, modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(PrimaryPurple.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                tint = PrimaryPurple,
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(email, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
        Text("ผู้ดูแลระบบ", fontSize = 14.sp, color = Color.Gray)
    }
}

@Composable
fun MenuButtonLong(title: String, icon: ImageVector, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(36.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(PrimaryPurple, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, color = PrimaryPurple, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
fun MenuButtonSquare(
    title: String,
    icon: ImageVector,
    modifier: Modifier,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier.aspectRatio(1f),
        shape = RoundedCornerShape(36.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (enabled) Color.White else Color(0xFFE0E0E0),
        ),
        elevation = CardDefaults.cardElevation(2.dp),
        onClick = { if (enabled) onClick() },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = if (enabled) PrimaryPurple else Color.Gray,
                modifier = Modifier.size(50.dp),
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = if (enabled) PrimaryPurple else Color.Gray,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatThaiDateTimeRange(start: String, end: String): String {
    return try {
        val zone = ZoneId.of("Asia/Bangkok")
        val startTime = OffsetDateTime.parse(start).atZoneSameInstant(zone)
        val endTime = OffsetDateTime.parse(end).atZoneSameInstant(zone)
        val thaiLocale = Locale("th", "TH")
        val dateFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", thaiLocale)
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", thaiLocale)
        val startYearBE = startTime.year + 543
        val endYearBE = endTime.year + 543
        val startDateText = startTime.format(dateFormatter).replace(startTime.year.toString(), startYearBE.toString())
        val endDateText = endTime.format(dateFormatter).replace(endTime.year.toString(), endYearBE.toString())
        "เริ่ม $startDateText (${startTime.format(timeFormatter)} น.)\nถึง $endDateText (${endTime.format(timeFormatter)} น.)"
    } catch (e: Exception) {
        ""
    }
}