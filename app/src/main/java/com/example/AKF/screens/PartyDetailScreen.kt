package com.example.projectmode

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.project_election.viewmodel.PartyViewModel



@Composable
fun PartyDetailScreen(
    navController: NavController,
    partyId: String,
    viewModel: PartyViewModel = viewModel()
) {

    LaunchedEffect(partyId) {

        val realId = if (partyId.isBlank()) "1" else partyId

        viewModel.loadPartyDetail(realId)
        viewModel.loadMembersByParty(realId)
    }

    // ⭐ เพิ่มบรรทัดนี้
    val party = viewModel.selectedParty
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
//        bottomBar = { AdminBottomNavigation() }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {

            // ===== HEADER =====
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            ) {

                // ⭐ รูปพื้นหลังพรรค
                if (!party?.image_poster.isNullOrEmpty()) {

                    Image(
                        painter = rememberAsyncImagePainter(
                            "http://10.0.2.2:3000/uploads/${party?.image_poster}"
                        ),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                // ⭐ overlay สีม่วงให้ตัวหนังสืออ่านง่าย
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(PrimaryPurple.copy(alpha = 0.35f))
                )

                // 🔙 Back + Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 16.dp, start = 12.dp)
                        .align(Alignment.TopStart)
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .size(40.dp)
                            .shadow(8.dp, CircleShape)
                            .background(Color.White, CircleShape)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "ย้อนกลับ",
                            tint = PrimaryPurple
                        )
                    }

//                    Text(
//                        text = party?.Party_name ?: "",
//                        color = PrimaryPurple,
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 20.sp   // ⭐ หัวข้อหลัก
//                    )
                }

                // ===== CARD =====

                Card(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = 40.dp)
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {

                        party?.let {

                            Row(verticalAlignment = Alignment.CenterVertically) {

                                CircleNumber(it.Party_number.toString())

                                Spacer(Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = it.Party_name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )

                                    Text(
                                        text = it.Party_description ?: "-",
                                        fontSize = 14.sp,
                                        color = Color.Gray
                                    )

                                    Text(
                                        text = "ดูเพิ่มเติม",
                                        color = PrimaryPurple,
                                        fontSize = 14.sp,
                                        modifier = Modifier.clickable {

                                            val realId = if (partyId.isBlank()) "1" else partyId

                                            navController.navigate("partyInfo/$realId")
                                        }
                                    )
                                }
                            }

                        }
                    }
                }

            }

            Spacer(Modifier.height(56.dp))


            // ===== ACTION BUTTONS =====
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {

                Button(
                    onClick = {
                        val realId = if (partyId.isBlank()) "1" else partyId
                        navController.navigate("editParty/$realId")
                    },
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                ) {
                    Icon(Icons.Default.Edit, null)
                    Spacer(Modifier.width(6.dp))
                    Text("แก้ไข", fontSize = 18.sp)
                }

                Spacer(Modifier.width(12.dp))

                Button(
                    onClick = {
                        val realId = if (partyId.isBlank()) "1" else partyId
                        navController.navigate("addCandidate/$realId")
                        Log.d("NavDebug", "Navigating to: addCandidate/$realId")

                              },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors( containerColor =  Color.Magenta)
                ) {
                    Icon(Icons.Default.Add, null)
                    Spacer(Modifier.width(6.dp))
                    Text("เพิ่มสมาชิก")
                }
            }

            Spacer(Modifier.height(16.dp))

            // ===== SEARCH =====
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(50),
                placeholder = { Text("ค้นหาชื่อสมาชิก", fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                singleLine = true
            )
            Spacer(Modifier.height(16.dp))

            Row(){
                Spacer(Modifier.height(12.dp))
                    Text(
                        "สมาชิกทั้งหมด",
                        modifier = Modifier.padding(horizontal = 26.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                Spacer(Modifier.weight(1f))
                Text(
                    " ${viewModel.memberList.size} คน",
                    modifier = Modifier.padding(horizontal = 26.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Blue
                )
            }
            Spacer(Modifier.height(8.dp))

            // ⭐ แสดงข้อมูลจาก API
//            viewModel.memberList.forEach { member ->
//                member.id?.let { safeId ->
//                    CandidateItem(
//                        name = member.fullName,
//                        position = getPositionName(member.positionId),
//                        profileImage = member.profileImage,   // ✅ เพิ่ม
//                        navController = navController,
//                        partyId = partyId,
//                        memberId = safeId
//                    )
//                }
//            }
            // ⭐ filter สมาชิกตามคำค้นหา
            val filteredMembers = viewModel.memberList.filter { member ->
                member.deleteAt == null &&   // 👈 แสดงเฉพาะที่ยังไม่ถูกลบ
                        member.fullName.contains(searchQuery, ignoreCase = true)
            }

            // ⭐ แสดงผลหลังกรองแล้ว
            filteredMembers.forEach { member ->
                member.id?.let { safeId ->
                    CandidateItem(
                        name = member.fullName,
                        position = member.positionName ?: "สมาชิกพรรค",
                        profileImage = member.profileImage,
                        navController = navController,
                        partyId = partyId,
                        memberId = safeId
                    )
                }
            }
        }
    }
}

@Composable
fun CircleNumber(number: String) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(PrimaryPurple),
        contentAlignment = Alignment.Center
    ) {
        Text(number, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}

@Composable
fun CandidateItem(name: String,
                  position: String,
                  profileImage: String?,   // ✅ เพิ่ม
                  navController: NavController,
                  partyId: String,
                  memberId: Int,
                  viewModel: PartyViewModel = viewModel()   // 👈 เพิ่ม
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current   // 👈 เพิ่มตรงนี้
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(PrimaryPurple.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {

                if (!profileImage.isNullOrBlank()) {

                    val imageUrl = "http://10.0.2.2:3000/uploads/$profileImage"

                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                } else {

                    Icon(
                        Icons.Default.Person,
                        null,
                        tint = PrimaryPurple
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(position, fontSize = 14.sp, color = Color.Gray)
            }

            IconButton(
                onClick = {
                    val realId = if (partyId.isBlank()) "1" else partyId
                    Log.d("NavDebug", "Navigating to: editCandidate/$realId/$memberId")
                    navController.navigate("editCandidate/$realId/$memberId")
                }
            ) {
                Icon(Icons.Default.Edit, null, tint = PrimaryPurple)
            }


            IconButton(
                onClick = {
                    showDeleteDialog = true
                }
            ) {
                Icon(Icons.Default.Delete, null, tint = LogoutRed)
            }

        }
    }
    Spacer(Modifier.height(22.dp))
    // 🔥 วาง AlertDialog ตรงนี้เลย
    if (showDeleteDialog) {

        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
            },
            title = {
                Text("ยืนยันการลบ")
            },
            text = {
                Text("คุณแน่ใจหรือไม่ว่าต้องการลบสมาชิกคนนี้?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteMember(memberId) { success ->
                            if (success) {

                                Toast.makeText(
                                    context,
                                    "ลบสมาชิกเรียบร้อยแล้ว",
                                    Toast.LENGTH_SHORT
                                ).show()

//                                viewModel.loadMembersByParty(partyId)

                            } else {

                                Toast.makeText(
                                    context,
                                    "เกิดข้อผิดพลาดในการลบ",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        showDeleteDialog = false
                    }
                ) {
                    Text("ลบ", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                    }
                ) {
                    Text("ยกเลิก")
                }
            }
        )
    }
}

// เพิ่มไว้นอก Composable function หรือในไฟล์ Member.kt
fun getPositionName(positionId: Int): String {
    return when (positionId) {
        1 -> "หัวหน้าพรรค"
        2 -> "รองหัวหน้าพรรค"
        else -> "สมาชิกพรรค"
    }
}


