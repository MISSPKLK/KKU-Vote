package com.example.projectmode
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ManageElectionScreen(
    navController: NavController,
    electionId: Int? = null   // ⭐ เพิ่มตรงนี้
) {

    val viewModel: ElectionViewModel = viewModel()
    val context = LocalContext.current

    val isoFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    var electionName by remember { mutableStateOf("") }

    var startDateTime by remember { mutableStateOf<LocalDateTime?>(null) }
    var endDateTime by remember { mutableStateOf<LocalDateTime?>(null) }

    var showDeleteDialog by remember { mutableStateOf(false) }

    val now = LocalDateTime.now()

    LaunchedEffect(electionId) {
        if (electionId != null) {
            viewModel.loadElectionById(electionId)
        }
    }

    LaunchedEffect(viewModel.selectedElection) {
        viewModel.selectedElection?.let { election ->

            electionName = election.election_name

            startDateTime = OffsetDateTime
                .parse(election.start_time)
                .atZoneSameInstant(ZoneId.systemDefault())
                .toLocalDateTime()

            endDateTime = OffsetDateTime
                .parse(election.end_time)
                .atZoneSameInstant(ZoneId.systemDefault())
                .toLocalDateTime()
        }
    }

    Scaffold(
//        bottomBar = { AdminBottomNavigation() }
    ) { paddingValues ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .background(LightPurpleBackground)
                .padding(paddingValues)   // ⭐ ใส่บรรทัดนี้
                .padding(horizontal = 22.dp),
        ) {

            Spacer(modifier = Modifier.height(46.dp))

            // 🔙 แถบหัวข้อ + ปุ่มกลับ
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "ย้อนกลับ",
                        tint = PrimaryPurple
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (electionId != null)
                        "แก้ไขการเลือกตั้ง"
                    else
                        "จัดการเลือกตั้ง",
                    color = PrimaryPurple,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 📝 ชื่อการเลือกตั้ง
            Text("ชื่อการเลือกตั้ง", fontWeight = FontWeight.Bold,  color = Color.DarkGray)

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = electionName,
                onValueChange = { electionName = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("กรอกชื่อการเลือกตั้ง") },
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                )
            )


            Spacer(modifier = Modifier.height(20.dp))

            Text("ระยะเวลาการลงคะแนน", fontWeight = FontWeight.Bold, color = Color.DarkGray)



            Spacer(modifier = Modifier.height(12.dp))

            SelectableDateTimeCard(
                title = "เริ่มการลงคะแนน",
                dateTime = startDateTime,
                onDateTimeSelected = { startDateTime = it }
            )

            Spacer(modifier = Modifier.height(12.dp))

            SelectableDateTimeCard(
                title = "สิ้นสุดการลงคะแนน",
                dateTime = endDateTime,
                onDateTimeSelected = { endDateTime = it }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ✅ ปุ่มเสร็จสิ้น
            Button(
                onClick = {

                    when {

                        electionName.isBlank() -> {
                            Toast.makeText(
                                context,
                                "กรุณากรอกชื่อการเลือกตั้ง",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        startDateTime == null || endDateTime == null -> {
                            Toast.makeText(
                                context,
                                "กรุณาเลือกวันเวลาให้ครบ",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        // 1. ห้ามเวลาเริ่มเป็นอดีต
                        // 1. ห้ามเวลาเริ่มเป็นอดีต (เฉพาะตอนสร้างใหม่)
                        electionId == null && startDateTime!!.isBefore(now) -> {
                            Toast.makeText(
                                context,
                                "เวลาเริ่มต้นต้องเป็นเวลาปัจจุบันหรืออนาคต",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        //  2. ห้ามเวลาสิ้นสุดก่อนเวลาเริ่ม
                        !endDateTime!!.isAfter(startDateTime) -> {
                            Toast.makeText(
                                context,
                                "เวลาสิ้นสุดต้องมากกว่าเวลาเริ่มต้น",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {

                            if (electionId != null) {

                                viewModel.updateElection(
                                    id = electionId,
                                    name = electionName,
                                    start = startDateTime!!
                                        .atZone(ZoneId.systemDefault())
                                        .toOffsetDateTime()
                                        .format(isoFormatter),

                                    end = endDateTime!!
                                        .atZone(ZoneId.systemDefault())
                                        .toOffsetDateTime()
                                        .format(isoFormatter)
                                ) {
                                    Toast.makeText(
                                        context,
                                        "แก้ไขการเลือกตั้งสำเร็จ",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    navController.popBackStack()
                                }

                            } else {

                                viewModel.createElection(
                                    name = electionName,
                                    start = startDateTime!!
                                        .atZone(ZoneId.systemDefault())
                                        .toOffsetDateTime()
                                        .format(isoFormatter),

                                    end = endDateTime!!
                                        .atZone(ZoneId.systemDefault())
                                        .toOffsetDateTime()
                                        .format(isoFormatter)
                                ) {
                                    Toast.makeText(
                                        context,
                                        "สร้างการเลือกตั้งสำเร็จ",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    navController.popBackStack()
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF8E97FD)
                )
            ) {
                Text(
                    if (electionId != null) "บันทึกการแก้ไข" else "เสร็จสิ้น",
                    fontSize = 18.sp,
                    color = Color.White
                )

            }


// 🔥 ปุ่มลบ (แสดงเฉพาะตอนแก้ไข)
            if (electionId != null) {

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LogoutRed),
                    shape = RoundedCornerShape(26.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 2.dp,   // ปกติ
                        pressedElevation = 6.dp,   // ตอนกด
                        disabledElevation = 0.dp
                    )
                ) {
                    Text(
                        "ลบการเลือกตั้ง",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }


        }
    }
    // 🔥 Dialog ต้องอยู่นอก Scaffold
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false

                        electionId?.let { id ->
                            viewModel.deleteElection(id) {
                                Toast.makeText(
                                    context,
                                    "ลบการเลือกตั้งสำเร็จ",
                                    Toast.LENGTH_SHORT
                                ).show()

                                navController.popBackStack()
                            }
                        }
                    }
                ) {
                    Text("ยืนยัน", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("ยกเลิก")
                }
            },
            title = { Text("ยืนยันการลบ") },
            text = { Text("คุณต้องการลบการเลือกตั้งนี้ใช่หรือไม่?") }
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SelectableDateTimeCard(
    title: String,
    dateTime: LocalDateTime?,
    onDateTimeSelected: (LocalDateTime) -> Unit
) {

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val thaiLocale = Locale("th", "TH")

    val dateFormatter = DateTimeFormatter.ofPattern("d MMM", thaiLocale)
    val timeFormatter = DateTimeFormatter.ofPattern("HH.mm", thaiLocale)

    val currentDate = dateTime ?: LocalDateTime.now()

    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.outlinedCardColors(containerColor = Color.White)
    ) {

        Column(Modifier.padding(16.dp)) {

            Text(title, color = Color.Gray)

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                // 📅 ปุ่มเลือกวันที่
                Row(
                    modifier = Modifier
                        .clickable {

                            DatePickerDialog(
                                context,
                                { _, year, month, day ->

                                    val updated = currentDate
                                        .withYear(year)
                                        .withMonth(month + 1)
                                        .withDayOfMonth(day)

                                    onDateTimeSelected(updated)

                                },
                                currentDate.year,
                                currentDate.monthValue - 1,
                                currentDate.dayOfMonth
                            ).show()

                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = PrimaryPurple
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = if (dateTime != null)
                            "${dateTime.format(dateFormatter)} ${dateTime.year + 543}"
                        else
                            "เพิ่มวันที่",
                        color = PrimaryPurple,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))

                // 🕒 ปุ่มเลือกเวลา
                Row(
                    modifier = Modifier
                        .clickable {

                            TimePickerDialog(
                                context,
                                { _, hour, minute ->

                                    val updated = currentDate
                                        .withHour(hour)
                                        .withMinute(minute)

                                    onDateTimeSelected(updated)

                                },
                                currentDate.hour,
                                currentDate.minute,
                                true
                            ).show()

                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = PrimaryPurple
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = if (dateTime != null)
                            "${dateTime.format(timeFormatter)} น."
                        else
                            "เพิ่มเวลา",
                        color = PrimaryPurple,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateTimePickerField(
    label: String,
    dateTime: LocalDateTime?,
    onDateTimeSelected: (LocalDateTime) -> Unit
) {
    val context = LocalContext.current

    val calendar = java.util.Calendar.getInstance()

    OutlinedButton(
        onClick = {
            DatePickerDialog(
                context,
                { _, year, month, day ->
                    TimePickerDialog(
                        context,
                        { _, hour, minute ->
                            val selected = LocalDateTime.of(
                                year,
                                month + 1,
                                day,
                                hour,
                                minute
                            )
                            onDateTimeSelected(selected)
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                    ).show()
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = dateTime?.format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            ) ?: label
        )
    }
}