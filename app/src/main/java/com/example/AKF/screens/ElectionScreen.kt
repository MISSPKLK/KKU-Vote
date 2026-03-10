package com.example.project_election.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.project_election.R
import com.example.project_election.components.AppBottomBar
import com.example.project_election.navigation.Routes
import com.example.projectmode.ElectionViewModel

@Composable
fun ElectionScreen(
    viewModel: ElectionViewModel,
    navController: NavController,
    currentRoute: String?,
) {
    val status by viewModel.status.collectAsState()


    LaunchedEffect(Unit) {
        viewModel.fetchStatus()
    }


    LaunchedEffect(status) {
        status?.id?.let { electionId ->
            viewModel.checkUserVotedStatus(electionId)
        }
    }

    when {
        status == null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFA53322))
            }
        }

        status?.status.equals("NOT_STARTED", ignoreCase = true) -> {
            if (status?.startTime.isNullOrEmpty() || status?.id == null) {
                EmptyElectionScreen(navController, currentRoute)
            } else {
                NotStartedScreen(
                    startTime = status?.startTime,
                    navController = navController,
                    currentRoute = currentRoute,
                    onStart = {
                        status?.endTime?.let {
                            val encoded = java.net.URLEncoder.encode(it, "UTF-8")
                            navController.navigate("countdown/$encoded")
                        }
                    },
                )
            }
        }

        status?.status.equals("OPEN", ignoreCase = true) -> {
            status?.endTime?.let {
                CountdownScreen(
                    endTime = it,
                    viewModel = viewModel,
                    navController = navController,
                    currentRoute = currentRoute,
                    onTimeUp = {
                        viewModel.fetchWinner()
                        navController.navigate(Routes.RESULT) {
                            popUpTo(navController.graph.startDestinationId)
                        }
                    },
                )
            }
        }

        status?.status.equals("CLOSED", ignoreCase = true) -> {
            ResultScreen(
                viewModel = viewModel,
                navController = navController,
                currentRoute = currentRoute,
            )
        }

        else -> {
            EmptyElectionScreen(navController, currentRoute)
        }
    }
}

@Composable
fun EmptyElectionScreen(navController: NavController, currentRoute: String?) {
    Scaffold(
        bottomBar = { AppBottomBar(navController, currentRoute) },
        containerColor = Color(0xFFF5F5FC),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_kku_vote),
                contentDescription = "No Election",
                modifier = Modifier.size(120.dp),
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "ยังไม่มีการเลือกตั้งในขณะนี้",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFA53322),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "โปรดรอประกาศจากคณะกรรมการบริหาร\nองค์การนักศึกษามหาวิทยาลัยขอนแก่น",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
            )
        }
    }
}