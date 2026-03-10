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
fun EditPartyScreen(
    navController: NavController,
    partyId: Int,
    viewModel: PartyViewModel = viewModel()
) {
    val context = LocalContext.current

    val policies = viewModel.policyList

    val logoLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? -> viewModel.logoUri.value = uri }

    val posterLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? -> viewModel.posterUri.value = uri }

    // โหลดข้อมูลพรรคเมื่อเปิดหน้า
    LaunchedEffect(partyId) {
        viewModel.loadPartyById(partyId)
        viewModel.loadPoliciesByParty(partyId.toString())
    }

    // เมื่อโหลดข้อมูลได้ ให้ set รูปเดิม
    LaunchedEffect(viewModel.editingParty.value) {
        viewModel.editingParty.value?.let { party ->
            party.logo?.let {
                viewModel.logoUri.value = Uri.parse("http://10.0.2.2:3000/uploads/$it")
            }
            party.image_poster?.let {
                viewModel.posterUri.value = Uri.parse("http://10.0.2.2:3000/uploads/$it")
            }
        }
    }

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
                    text = "แก้ไขพรรค",
                    color = PrimaryPurple,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

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

            FieldLabel("โลโก้")
            ImageUploadBox(
                imageUri = viewModel.logoUri.value,
                maxCount = 1,
                onClick = { logoLauncher.launch("image/*") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            FieldLabel("รูปภาพโปสเตอร์")
            ImageUploadBox(
                imageUri = viewModel.posterUri.value,
                maxCount = 1,
                onClick = { posterLauncher.launch("image/*") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            FieldLabel("นโยบาย")
            PolicySection(
                policies = policies.map {
                    com.example.project_election.components.Policy(
                        title = it.policy_title,
                        detail = it.policy_description ?: ""
                    )
                },
                onTitleChange = { _, _ -> },   // edit นโยบายใน edit party ยังไม่ implement
                onDetailChange = { _, _ -> },
                onAddPolicy = {}
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    viewModel.updateParty(partyId, context) {
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8E97FD)),
                elevation = ButtonDefaults.buttonElevation(6.dp)
            ) {
                Text("อัปเดต", fontSize = 18.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
