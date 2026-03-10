package com.example.project_election.screens

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import com.example.project_election.models.UserSearchResult
import com.example.project_election.viewmodel.PartyViewModel
import com.example.projectmode.LightPurpleBackground
import com.example.projectmode.PrimaryPurple
import kotlin.collections.find
import kotlin.collections.forEach
import kotlin.let
import kotlin.text.isBlank
import kotlin.text.removePrefix
import kotlin.text.startsWith
import kotlin.text.trim



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMemberScreen(
    navController: NavController,
    partyId: String,
    memberId: Int? = null,
    viewModel: PartyViewModel = viewModel()
){

    val party = viewModel.selectedParty

    var prefixExpanded by rememberSaveable  { mutableStateOf(false) }
    var selectedPrefix by rememberSaveable  { mutableStateOf("") }

    val prefixList = listOf("นาย", "นาง", "นางสาว")

    var positionExpanded by rememberSaveable  { mutableStateOf(false) }
    var selectedPositionId by rememberSaveable { mutableStateOf<Int?>(null) }
    val selectedPosition =
        viewModel.positionList.find { it.idParty_positions == selectedPositionId }

    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var memberNumber by rememberSaveable { mutableStateOf("") }
    var biography by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable  { mutableStateOf("") }

    var emailExpanded by rememberSaveable  { mutableStateOf(false) }

    var selectedUser by rememberSaveable  { mutableStateOf<UserSearchResult?>(null) }

    val context = LocalContext.current

    var imageUri by rememberSaveable(
        stateSaver = Saver(
            save = { it?.toString() },
            restore = { it?.let { Uri.parse(it) } }
        )
    ) { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }



    Scaffold(
//        bottomBar = { AdminBottomNavigation() }
    ) { paddingValues ->

        // 1. ดึงข้อมูลจาก API เมื่อมี memberId ส่งมา
        LaunchedEffect(memberId) {
            viewModel.loadPartyDetail(partyId)
            viewModel.loadPositions()
            memberId?.let {
                viewModel.loadMemberById(it)
            }
        }

        LaunchedEffect(selectedUser) {
            Log.d("DEBUG_ADD", "selectedUser = $selectedUser")
            Log.d("DEBUG_ADD", "userId = ${selectedUser?.idUsers}")
        }

        LaunchedEffect(viewModel.editingMember) {
            viewModel.editingMember?.let { member ->

                selectedUser = UserSearchResult(
                    idUsers = member.userId ?: "",
                    email = member.email ?: ""
                )
                val prefixList = listOf("นาย", "นาง", "นางสาว")

                selectedPrefix = member.prefix ?: ""
                firstName = member.firstName ?: ""

                lastName = member.lastName ?: ""
                memberNumber = member.memberNumber ?: ""
                biography = member.biography ?: ""
                selectedPositionId = member.positionId

                // 👇 เพิ่มตรงนี้
                member.profileImage?.let { fileName ->
                    imageUri = Uri.parse("http://10.0.2.2:3000/uploads/$fileName")
                }
            }
        }



        LaunchedEffect(Unit) {
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.getLiveData<UserSearchResult>("selected_user")
                ?.observeForever { user ->
                    selectedUser = user
                }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LightPurpleBackground)
                .verticalScroll(rememberScrollState())   // ⭐ เพิ่มบรรทัดนี้
                .padding(paddingValues)
                .padding(horizontal = 22.dp)
        ) {

            Spacer(modifier = Modifier.height(46.dp))

            // 🔙 Top bar
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
                    text = if (memberId == null) "เพิ่มผู้สมัคร" else "แก้ไขผู้สมัคร",
                    color = PrimaryPurple,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // พรรค
            FieldLabel("พรรค")

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(
                        color = Color(0xFFF2F2F2),   // เทาอ่อน
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = party?.Party_name ?: "",
                    color = Color.DarkGray,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // หมายเลขสมาชิก
            FieldLabel("หมายเลขสมาชิก")
            InputField(
                value = memberNumber,
                onValueChange = { memberNumber = it },
                placeholder = "กรอกหมายเลขสมาชิก"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ชื่อผู้สมัคร

            FieldLabel("ชื่อผู้สมัคร")

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                // 🔽 คำนำหน้า
                ExposedDropdownMenuBox(
                    expanded = prefixExpanded,
                    onExpandedChange = { prefixExpanded = !prefixExpanded },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = selectedPrefix,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("คำนำหน้า") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = prefixExpanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = prefixExpanded,
                        onDismissRequest = { prefixExpanded = false }
                    ) {
                        prefixList.forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    selectedPrefix = it
                                    prefixExpanded = false
                                }
                            )
                        }
                    }
                }

                InputField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    placeholder = "ชื่อ",
                    modifier = Modifier.weight(0.8f)
                )

                InputField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    placeholder = "นามสกุล",
                    modifier = Modifier.weight(0.8f)
                )

            }

            Spacer(modifier = Modifier.height(16.dp))

            // ตำแหน่ง
            FieldLabel("ตำแหน่ง")

            ExposedDropdownMenuBox(
                expanded = positionExpanded,
                onExpandedChange = { positionExpanded = !positionExpanded }
            ) {
                OutlinedTextField(
                    value = selectedPosition?.position_name ?: "",
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("เลือกตำแหน่ง") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = positionExpanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )

                ExposedDropdownMenu(
                    expanded = positionExpanded,
                    onDismissRequest = { positionExpanded = false }
                ) {
                    viewModel.positionList.forEach { position ->
                        DropdownMenuItem(
                            text = { Text(position.position_name) },
                            onClick = {
                                selectedPositionId = position.idParty_positions
                                positionExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ประวัติ
            FieldLabel("ประวัติส่วนตัว")
            OutlinedTextField(
                value = biography,
                onValueChange = { biography = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                placeholder = { Text("กรอกประวัติส่วนตัว") },
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // รูปภาพ
            FieldLabel("รูปภาพผู้สมัคร")

            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clickable {
                        imagePickerLauncher.launch("image/*")
                    },
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    if (imageUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(imageUri),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            }

            FieldLabel("อีเมล")

            Button(
                onClick = {
                    navController.navigate("search_user")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp), // ความสูงเหมือน TextField
                shape = RoundedCornerShape(20.dp), // ปรับให้โค้งเหมือนในรูป
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFFFFF), // เทาอ่อน
                    contentColor = Color.Gray
                ),
                elevation = ButtonDefaults.buttonElevation(0.dp),
                border = BorderStroke(1.dp, Color(0xFFCCCCCC)) // เส้นขอบเหมือน TextField
            ) {
                Text(
                    text = selectedUser?.email ?: "ค้นหาอีเมล",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {

                    // ✅ Validate
                    if (memberNumber.isBlank()) {
                        Toast.makeText(context, "กรุณากรอกหมายเลขสมาชิก", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (firstName.isBlank()) {
                        Toast.makeText(context, "กรุณากรอกชื่อ", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (lastName.isBlank()) {
                        Toast.makeText(context, "กรุณากรอกนามสกุล", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (selectedPosition == null) {
                        Toast.makeText(context, "กรุณาเลือกตำแหน่ง", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (selectedUser == null) {
                        Toast.makeText(context, "กรุณาเลือกอีเมลผู้ใช้", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    if (memberId == null) {

                        viewModel.createMemberMultipart(
                            context = context,
                            imageUri = imageUri,
                            userId = selectedUser!!.idUsers,
                            partyId = partyId,
                            positionId = selectedPosition!!.idParty_positions,
                            prefix = selectedPrefix,
                            firstName = firstName,
                            lastName = lastName,
                            memberNumber = memberNumber,
                            biography = biography,
                            adminId = "ADMIN000001"   // ⭐ fix admin
                        ) { success ->

                            if (success) {
                                Toast.makeText(context, "เพิ่มผู้สมัครสำเร็จ", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            } else {
                                Toast.makeText(context, "เพิ่มไม่สำเร็จ", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {

                        // ก่อนส่งข้อมูลอัปเดต ให้ตรวจสอบว่า firstName ไม่ได้มีคำนำหน้าติดไปแล้ว
                        val finalFirstName = if (firstName.startsWith(selectedPrefix)) {
                            firstName.removePrefix(selectedPrefix).trim()
                        } else {
                            firstName
                        }

                        viewModel.updateMemberMultipart(
                            context = context,
                            memberId = memberId,
                            imageUri = imageUri,
                            userId = selectedUser!!.idUsers,
                            partyId = partyId,
                            positionId = selectedPosition!!.idParty_positions,
                            prefix = selectedPrefix,
                            firstName = finalFirstName,
                            lastName = lastName,
                            memberNumber = memberNumber,
                            biography = biography
                        ) { success ->
                            if (success) {
                                Toast.makeText(context, "อัปเดตสำเร็จ", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            } else {
                                Toast.makeText(context, "อัปเดตไม่สำเร็จ", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8E97FD)),
                elevation = ButtonDefaults.buttonElevation(6.dp)
            ) {
                Text(
                    if (memberId == null) "เสร็จสิ้น" else "อัปเดต",
                    fontSize = 18.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun FieldLabel(text: String) {
    Text(
        text,
        fontWeight = FontWeight.Bold,
        color = Color.DarkGray
    )
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = { Text(placeholder) },
        shape = RoundedCornerShape(16.dp),
        singleLine = true
    )
}

@Composable
fun DropdownField(text: String) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text, color = Color.Gray)
            Icon(Icons.Default.KeyboardArrowDown, null)
        }
    }
}