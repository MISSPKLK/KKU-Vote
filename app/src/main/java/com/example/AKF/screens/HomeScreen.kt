package com.example.project_election.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.rounded.Article
import androidx.compose.material.icons.rounded.LocalActivity
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.project_election.network.NotificationResponse
import com.google.firebase.auth.FirebaseAuth

data class NewsItem(
    val id: Int,
    val imageRes: Int,
    val title: String,
    val subtitle: String,
    val date: String,
    val tag: String,
    val tagColor: Color,
    val body: String
)

data class ActivityItem(
    val imageRes: Int,
    val label: String,
    val partyId: Int,
    val partyName: String
)

data class NotificationItem(
    val title: String,
    val time: String,
    val isUnread: Boolean
)

val newsList = listOf(
    NewsItem(
        id = 1,
        imageRes = R.drawable.banner_election_2569,
        title = "เลือกตั้งนายกองค์การนักศึกษา ปีการศึกษา 2569",
        subtitle = "เลือดสีอิฐ vs กาลพฤกษ์",
        date = "2 กุมภาพันธ์ 2569",
        tag = "ประกาศ",
        tagColor = Color(0xFFA53322),
        body = """คณะกรรมการบริหารองค์การนักศึกษามหาวิทยาลัยขอนแก่น ประกาศให้นักศึกษาทุกคนใช้สิทธิ์เลือกตั้งนายกองค์การนักศึกษา ประจำปีการศึกษา 2569

📌 กลุ่มผู้สมัคร
• เบอร์ 1 — กลุ่มเลือดสีอิฐ
• เบอร์ 2 — กลุ่มกาลพฤกษ์ 2026

🗓 วันเลือกตั้ง: วันที่ 2 กุมภาพันธ์ 2569
⏰ เวลา: 08:00 – 17:00 น.
🌐 ช่องทาง: KKUMAIL

นักศึกษาสามารถเข้าลงคะแนนได้ผ่านระบบออนไลน์ที่ kkumail.com โดยใช้รหัสนักศึกษาของตนเอง"""
    ),
    NewsItem(
        id = 2,
        imageRes = R.drawable.banner_luead_see_id_group,
        title = "ประกาศรายชื่อผู้มีสิทธิ์เลือกตั้ง",
        subtitle = "ตรวจสอบสิทธิ์ของคุณได้แล้ววันนี้",
        date = "28 มกราคม 2569",
        tag = "ข่าวสาร",
        tagColor = Color(0xFF4285F4),
        body = """นักศึกษาสามารถตรวจสอบรายชื่อผู้มีสิทธิ์เลือกตั้งได้ที่ระบบ KKU Vote

หากพบว่าชื่อของท่านไม่ปรากฏในรายชื่อ กรุณาติดต่องานกิจการนักศึกษาภายในวันที่ 1 กุมภาพันธ์ 2569

📞 สอบถามข้อมูลเพิ่มเติม: กองกิจการนักศึกษา อาคาร KKU โทร 043-202-660"""
    ),
    NewsItem(
        id = 3,
        imageRes = R.drawable.banner_kalapruek,
        title = "นโยบายกลุ่มเลือดสีอิฐ เบอร์ 1",
        subtitle = "ปฏิรูปสวัสดิการนักศึกษา",
        date = "25 มกราคม 2569",
        tag = "นโยบาย",
        tagColor = Color(0xFFE53935),
        body = """กลุ่มเลือดสีอิฐ เสนอนโยบายหลัก 5 ด้าน

1. 🏥 สวัสดิการสุขภาพ — เพิ่มงบประกันสุขภาพนักศึกษา
2. 📚 ทุนการศึกษา — ขยายโควตาทุนฉุกเฉิน
3. 🚌 การเดินทาง — รถรับส่งฟรีในมหาวิทยาลัย
4. 💼 Internship — เชื่อมโยงนักศึกษากับบริษัทชั้นนำ
5. 🎤 เสียงนักศึกษา — เปิดช่องทางร้องเรียนตรงถึงผู้บริหาร"""
    )
)

val activityList = listOf(
    ActivityItem(
        imageRes = R.drawable.banner_luead_see_id_group,
        label = "กลุ่มเลือดสีอิฐ",
        partyId = 1,
        partyName = "เลือดสีอิฐ"
    ),
    ActivityItem(
        imageRes = R.drawable.banner_kalapruek,
        label = "กลุ่มกาลพฤกษ์",
        partyId = 2,
        partyName = "กาลพฤกษ์"
    )
)

val mockNotifications = listOf(
    NotificationItem("เริ่มการเลือกตั้งนายกองค์การนักศึกษา 2569 แล้ว ไปใช้สิทธิ์กันเลย!", "10 นาทีที่แล้ว", true),
    NotificationItem("กลุ่มกาลพฤกษ์ ได้เพิ่มนโยบายใหม่ในระบบ", "2 ชั่วโมงที่แล้ว", false),
    NotificationItem("ประกาศรายชื่อผู้มีสิทธิ์เลือกตั้งอย่างเป็นทางการ", "1 วันที่แล้ว", false)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, currentRoute: String?) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userEmail = currentUser?.email ?: "Guest"
    val usernamePrefix = userEmail.substringBefore("@")

    var selectedNews by remember { mutableStateOf<NewsItem?>(null) }
    var showNotiSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()


    var notifications by remember { mutableStateOf<List<NotificationResponse>>(emptyList()) }


    LaunchedEffect(showNotiSheet) {
        if (showNotiSheet) {

            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                try {
                    val client = okhttp3.OkHttpClient()
                    val request = okhttp3.Request.Builder()

                        .url("http://10.0.2.2:3000/api/notifications")
                        .build()

                    val response = client.newCall(request).execute()

                    if (response.isSuccessful) {
                        val jsonData = response.body?.string()
                        if (jsonData != null) {
                            val jsonArray = org.json.JSONArray(jsonData)
                            val fetchedNotifications = mutableListOf<NotificationResponse>()

                            for (i in 0 until jsonArray.length()) {
                                val obj = jsonArray.getJSONObject(i)
                                fetchedNotifications.add(
                                    NotificationResponse(
                                        title = obj.getString("title"),
                                        message = obj.getString("message"),
                                        created_at = obj.getString("created_at")
                                    )
                                )
                            }

                            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                                notifications = fetchedNotifications
                            }
                        }
                    }
                } catch (e: Exception) {

                    android.util.Log.e("API_ERROR", "Detail: ${e.localizedMessage}")
                }
            }
        }
    }

    if (selectedNews != null) {
        NewsDetailScreen(news = selectedNews!!, onBack = { selectedNews = null })
        return
    }

    Scaffold(
        bottomBar = { AppBottomBar(navController, currentRoute) },
        containerColor = Color(0xFFF5F5FC)
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).verticalScroll(rememberScrollState())
        ) {
            HeaderSection(username = usernamePrefix, onNotiClick = { showNotiSheet = true })
            VoteStatusCard()
            Spacer(modifier = Modifier.height(24.dp))
            SectionTitle("กิจกรรมมาแรง", Icons.Rounded.LocalActivity) { navController.navigate(Routes.TRENDING_EVENTS) }
            TrendingEventsSection { activity ->
                navController.navigate(Routes.candidateDetailRoute(activity.partyId, activity.partyName))
            }
            Spacer(modifier = Modifier.height(28.dp))
            SectionTitle("ข่าวสารและประกาศ", Icons.Rounded.Article) { navController.navigate(Routes.NEWS_LIST) }
            NewsSection { news -> selectedNews = news }
            Spacer(modifier = Modifier.height(32.dp))
        }

        if (showNotiSheet) {
            ModalBottomSheet(
                onDismissRequest = { showNotiSheet = false },
                sheetState = sheetState,
                containerColor = Color.White
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp)) {
                    Text("การแจ้งเตือน", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn(
                        modifier = Modifier.heightIn(max = 400.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(notifications) { noti ->
                            NotificationRowReal(noti)
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun NotificationRowReal(noti: NotificationResponse) {
    Row(
        modifier = Modifier.fillMaxWidth().background(Color(0xFFFDECEA), RoundedCornerShape(12.dp)).padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFFA53322).copy(0.1f)), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Notifications, null, tint = Color(0xFFA53322), modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = noti.title, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = noti.message, fontSize = 12.sp, color = Color.DarkGray, maxLines = 2)
            Text(text = noti.created_at, fontSize = 10.sp, color = Color.Gray)
        }
    }
}

@Composable
fun HeaderSection(username: String, onNotiClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = "สวัสดี 👋", color = Color.Gray, fontSize = 16.sp)
            Text(
                text = username,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color(0xFFA53322)
            )
        }
        IconButton(
            onClick = onNotiClick,
            modifier = Modifier
                .size(48.dp)
                .background(Color.White, shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = Color(0xFFA53322)
            )
        }
    }
}

@Composable
fun VoteStatusCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFA53322)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "เลือกตั้งนายกองค์การนักศึกษา", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "วันที่ 10 ม.ค. 2569", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            Image(
                painter = painterResource(id = R.drawable.logo_kku_vote),
                contentDescription = "Vote Logo",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(0.dp)
            )
        }
    }
}

@Composable
fun SectionTitle(title: String, icon: ImageVector, onSeeAllClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = Color(0xFFA53322), modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black)
        }
        Text(
            text = "ดูทั้งหมด",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.clickable { onSeeAllClick() }
        )
    }
}

@Composable
fun TrendingEventsSection(onActivityClick: (ActivityItem) -> Unit) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 20.dp)
    ) {
        items(activityList) { activity ->
            Card(
                modifier = Modifier
                    .width(280.dp)
                    .height(160.dp)
                    .clickable { onActivityClick(activity) },
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = activity.imageRes),
                        contentDescription = activity.label,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.55f)),
                                    startY = 60f
                                )
                            )
                    )
                    Text(
                        text = activity.label,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(12.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun NewsSection(onNewsClick: (NewsItem) -> Unit = {}) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 20.dp)
    ) {
        items(newsList) { news ->
            NewsCard(news = news, onClick = { onNewsClick(news) })
        }
    }
}

@Composable
fun NewsCard(news: NewsItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(210.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                Image(
                    painter = painterResource(id = news.imageRes),
                    contentDescription = news.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopStart)
                        .background(news.tagColor, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(text = news.tag, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = news.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = news.date,
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
        }
    }
}