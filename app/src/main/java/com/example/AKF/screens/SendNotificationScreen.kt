package com.example.project_election.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendNotificationScreen(navController: NavController) {
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isSending by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("ยิงแจ้งเตือนประกาศ", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(20.dp)) {
            Text("ส่งข้อความแจ้งเตือนถึงนักศึกษาทุกคน", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("หัวข้อ (Title)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("เนื้อหาข้อความ (Message)") },
                modifier = Modifier.fillMaxWidth().height(150.dp),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    isSending = true
                    scope.launch {
                        val result = sendNotificationToBackend(title, message)
                        isSending = false
                        if (result) {
                            title = ""
                            message = ""
                            snackbarHostState.showSnackbar("ส่งแจ้งเตือนสำเร็จแล้ว!")
                        } else {
                            snackbarHostState.showSnackbar("ส่งไม่สำเร็จ กรุณาเช็ค Server")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA53322)),
                enabled = title.isNotBlank() && message.isNotBlank() && !isSending
            ) {
                if (isSending) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Icon(Icons.Default.Send, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("ส่งแจ้งเตือนทันที", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

suspend fun sendNotificationToBackend(title: String, message: String): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            val client = OkHttpClient()
            val json = JSONObject().apply {
                put("title", title)
                put("message", message)
            }
            val body = json.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url("http://10.0.2.2:3000/api/send-notification")
                .post(body)
                .build()

            client.newCall(request).execute().use { response ->
                response.isSuccessful
            }
        } catch (e: Exception) {
            android.util.Log.e("SEND_NOTI_ERROR", e.message.toString())
            false
        }
    }
}