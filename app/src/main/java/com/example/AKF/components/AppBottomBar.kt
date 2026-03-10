package com.example.project_election.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.project_election.navigation.Routes

private val Purple = Color(0xFFAEABFF)
private val IconColor = Color(0xFF6B3A2A)

@Composable
fun AppBottomBar(navController: NavController, currentRoute: String?) {
    NavigationBar(
        containerColor = Purple,
        contentColor = IconColor
    ) {
        val itemColors = NavigationBarItemDefaults.colors(
            selectedIconColor = IconColor,
            selectedTextColor = IconColor,
            indicatorColor = Color.White.copy(alpha = 0.35f),
            unselectedIconColor = IconColor,
            unselectedTextColor = IconColor
        )

        NavigationBarItem(
            selected = currentRoute?.startsWith("home") == true,
            onClick = {
                navController.navigate(Routes.HOME) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("หน้าหลัก") },
            colors = itemColors
        )

        NavigationBarItem(
            selected = currentRoute == Routes.PARTY_LIST,
            onClick = {
                if (currentRoute != Routes.PARTY_LIST) {
                    navController.navigate(Routes.PARTY_LIST) {
                        launchSingleTop = true
                    }
                }
            },
            icon = { Icon(Icons.Default.Groups, contentDescription = "Parties") },
            label = { Text("พรรค") },
            colors = itemColors
        )

        NavigationBarItem(
            selected = currentRoute == Routes.ELECTION,
            onClick = {
                navController.navigate(Routes.ELECTION) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = { Icon(Icons.Default.HowToVote, contentDescription = "Vote") },
            label = { Text("โหวต") },
            colors = itemColors
        )

        NavigationBarItem(
            selected = currentRoute == Routes.SETTINGS,
            onClick = {
                if (currentRoute != Routes.SETTINGS) {
                    navController.navigate(Routes.SETTINGS) {
                        launchSingleTop = true
                    }
                }
            },
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("ตั้งค่า") },
            colors = itemColors
        )
    }
}