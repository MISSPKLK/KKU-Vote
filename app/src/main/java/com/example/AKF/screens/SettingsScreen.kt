package com.example.project_election.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.project_election.components.AppBottomBar
import com.example.project_election.navigation.Routes

@Composable
fun SettingsScreen(navController: NavController, currentRoute: String?) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = { AppBottomBar(navController, currentRoute) },
        containerColor = Color(0xFFF5F5FC)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Text(
                text = "ตั้งค่า",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color(0xFFA53322)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "ทั่วไป", color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column {
                    SettingsItem(
                        icon = Icons.Default.Notifications,
                        title = "การแจ้งเตือน",
                        onClick = { navController.navigate(Routes.NOTIFICATIONS_SETTING) }
                    )
                    Divider(color = Color(0xFFF0F0F0))
                    SettingsItem(
                        icon = Icons.Default.HelpOutline,
                        title = "ช่วยเหลือและสนับสนุน",
                        onClick = { navController.navigate(Routes.HELP_SUPPORT) }
                    )
                    Divider(color = Color(0xFFF0F0F0))
                    SettingsItem(
                        icon = Icons.Default.Info,
                        title = "เกี่ยวกับแอปพลิเคชัน",
                        onClick = { navController.navigate(Routes.ABOUT_APP) }
                    )
                }

            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "บัญชี", color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                SettingsItem(
                    icon = Icons.Default.ExitToApp,
                    title = "ออกจากระบบ",
                    textColor = Color(0xFFF0625D),
                    iconColor = Color(0xFFF0625D),
                    onClick = { showLogoutDialog = true }
                )
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("ออกจากระบบ") },
            text = { Text("คุณต้องการออกจากระบบหรือไม่") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false

                        // ออกจากระบบทั้งหมด
                        val auth = com.google.firebase.auth.FirebaseAuth.getInstance()
                        auth.signOut()

                        navController.navigate(Routes.LOGIN) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                ) {
                    Text("ออกจากระบบ", color = Color(0xFFF0625D))
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("ยกเลิก", color = Color.Gray)
                }
            }
        )
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    textColor: Color = Color.Black,
    iconColor: Color = Color.Gray,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, fontSize = 16.sp, color = textColor, modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
    }
}