package com.example.project_election.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project_election.components.ImageUploadBox
import com.example.project_election.components.Policy
import com.example.project_election.components.PolicySection
import com.example.project_election.viewmodel.PartyViewModel
import com.example.projectmode.LightPurpleBackground
import com.example.projectmode.PrimaryPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPartyScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: PartyViewModel = viewModel()

    var policies by remember { mutableStateOf(listOf(Policy())) }

    val logoLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? -> viewModel.logoUri.value = uri }

    val posterLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? -> viewModel.posterUri.value = uri }

    Scaffold { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LightPurpleBackground)
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(horizontal = 22.dp)
        ) {

            Spacer(modifier = Modifier.height(46.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "ย้อนกลับ",
                        tint = PrimaryPurple
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "เพิ่มพรรค",
                    color = PrimaryPurple,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ชื่อพรรค
            FieldLabel("ชื่อพรรค")
            OutlinedTextField(
                value = viewModel.partyName.value,
                onValueChange = { viewModel.partyName.value = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("กรอกชื่อพรรค") },
                supportingText = { Text("${viewModel.partyName.value.length}/100") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ชื่อพรรคภาษาอังกฤษ
            FieldLabel("ชื่อพรรคภาษาอังกฤษ")
            OutlinedTextField(
                value = viewModel.partyPrefixName.value,
                onValueChange = { viewModel.partyPrefixName.value = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("กรอกชื่อพรรคภาษาอังกฤษ") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // หมายเลขพรรค
            FieldLabel("หมายเลขพรรค")
            OutlinedTextField(
                value = viewModel.partyNumber.value,
                onValueChange = { viewModel.partyNumber.value = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("กรอกหมายเลขพรรค") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // คำอธิบาย
            FieldLabel("คำอธิบาย")
            OutlinedTextField(
                value = viewModel.description.value,
                onValueChange = { viewModel.description.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                placeholder = { Text("กรอกคำอธิบาย") },
                supportingText = { Text("${viewModel.description.value.length}/500") },
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // โลโก้
            FieldLabel("โลโก้")
            ImageUploadBox(
                imageUri = viewModel.logoUri.value,
                maxCount = 1,
                onClick = { logoLauncher.launch("image/*") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // รูปภาพโปสเตอร์
            FieldLabel("รูปภาพโปสเตอร์")
            ImageUploadBox(
                imageUri = viewModel.posterUri.value,
                maxCount = 1,
                onClick = { posterLauncher.launch("image/*") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // นโยบาย
            FieldLabel("นโยบาย")
            PolicySection(
                policies = policies,
                onTitleChange = { index, value ->
                    policies = policies.toMutableList().also {
                        it[index] = it[index].copy(title = value)
                    }
                },
                onDetailChange = { index, value ->
                    policies = policies.toMutableList().also {
                        it[index] = it[index].copy(detail = value)
                    }
                },
                onAddPolicy = {
                    policies = policies + Policy()
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ปุ่มเสร็จสิ้น
            Button(
                onClick = {
                    viewModel.addParty(context) {
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8E97FD)),
                elevation = ButtonDefaults.buttonElevation(6.dp)
            ) {
                Text("เสร็จสิ้น", fontSize = 18.sp, color = Color.White)
            }

            if (viewModel.duplicateError.value.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = viewModel.duplicateError.value,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}