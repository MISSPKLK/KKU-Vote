package com.example.project_election.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.project_election.R
import com.example.project_election.components.AppBottomBar
import com.example.project_election.navigation.Routes
import com.example.project_election.models.Party
import com.example.project_election.network.RetrofitInstance

// ─────────────────────────────────────────────
// Data model สำหรับนโยบาย
// ─────────────────────────────────────────────

data class PolicyItem(
    val imageRes: Int,
    val title: String,
    val partyId: Int,
    val partyName: String,
    val badgeColor: Color
)


val partyPolicyList = listOf(
    PolicyItem(
        imageRes = R.drawable.policy_lsi_1,
        title = "เส้นทางสู่ความสำเร็จ\nผู้ประกอบการเชิงสร้างสรรค์",
        partyId = 1,
        partyName = "เลือดสีอิฐ",
        badgeColor = Color(0xFFA53322)
    ),
    PolicyItem(
        imageRes = R.drawable.policy_image,
        title = "พัฒนามหาวิทยาลัยสู่\nความยั่งยืน",
        partyId = 2,
        partyName = "กาลพฤกษ์",
        badgeColor = Color(0xFF43A047)
    ),
    PolicyItem(
        imageRes = R.drawable.policy_image,
        title = "ปฏิรูปสวัสดิการนักศึกษา\nเพื่อคุณภาพชีวิตที่ดี",
        partyId = 1,
        partyName = "เลือดสีอิฐ",
        badgeColor = Color(0xFFA53322)
    ),
    PolicyItem(
        imageRes = R.drawable.policy_image,
        title = "นวัตกรรมและดิจิทัล\nเพื่อนักศึกษา",
        partyId = 2,
        partyName = "กาลพฤกษ์",
        badgeColor = Color(0xFF43A047)
    )
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartyListScreen(navController: NavController, currentRoute: String?) {
    var parties by remember { mutableStateOf<List<Party>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            parties = RetrofitInstance.api.getParties()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("พรรคการเมือง", fontWeight = FontWeight.Bold) },
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
                text = "เลือกพรรคที่คุณสนใจ",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 12.dp)
            )

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(240.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFA53322))
                }
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp)
                ) {
                    items(parties) { party ->
                        PartyCard(
                            party = party,
                            onClick = {
                                navController.navigate(
                                    Routes.candidateDetailRoute(party.idParties, party.Party_name)
                                )
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "นโยบายที่น่าสนใจ",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 20.dp, bottom = 12.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 20.dp)
            ) {
                items(partyPolicyList) { policy ->
                    PolicyCard(
                        policy = policy,
                        onClick = {
                            navController.navigate(
                                Routes.candidateDetailRoute(policy.partyId, policy.partyName)
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}



@Composable
fun PartyCard(party: Party, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(170.dp)
            .height(250.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = party.Party_name,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "เบอร์ ${party.Party_number}",
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA53322)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("ทำความรู้จัก", fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.weight(1f))

            // โลโก้พรรค — ขนาดคงที่เท่ากัน
            val logoRes = if (party.idParties == 2) R.drawable.logo_kalapruek else R.drawable.logo_lsi
            Image(
                painter = painterResource(id = logoRes),
                contentDescription = "Party Logo",
                modifier = Modifier.size(90.dp)
            )
        }
    }
}

// ─────────────────────────────────────────────
// PolicyCard — รูปต่างกันแต่ละพรรค ขนาด 160 x 260 dp ทุกใบ
// ─────────────────────────────────────────────

@Composable
fun PolicyCard(policy: PolicyItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(260.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // รูปโปสเตอร์นโยบาย
            Image(
                painter = painterResource(id = policy.imageRes),
                contentDescription = policy.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // gradient overlay ด้านล่าง
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.65f)),
                            startY = 130f
                        )
                    )
            )

            // badge ชื่อพรรค
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.TopEnd)
                    .background(policy.badgeColor, RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = policy.partyName,
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // ชื่อนโยบาย
            Text(
                text = policy.title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                lineHeight = 17.sp,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp)
            )
        }
    }
}