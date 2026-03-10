package com.example.project_election.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project_election.viewmodel.PartyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchUserScreen(
    navController: NavController,
    viewModel: PartyViewModel = viewModel()
) {
    var keyword by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        viewModel.searchUser("")
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ค้นหาผู้ใช้") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "ย้อนกลับ"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            // 🔎 ช่องค้นหา
            OutlinedTextField(
                value = keyword,
                onValueChange = {
                    keyword = it
                    viewModel.searchUser(it)
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("พิมพ์ค้นหา email") },
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 📌 แสดงจำนวนผลลัพธ์
            Text(
                text = "พบ ${viewModel.userSearchList.size} รายการ",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 📋 รายการผลลัพธ์
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(viewModel.userSearchList) { user ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        onClick = {
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("selected_user", user)

                            navController.popBackStack()
                        }
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = user.email,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}