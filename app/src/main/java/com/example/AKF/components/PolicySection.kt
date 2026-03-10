package com.example.project_election.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Policy(
    var title: String = "",
    var detail: String = ""
)

@Composable
fun PolicySection(
    policies: List<Policy>,
    onTitleChange: (Int, String) -> Unit,
    onDetailChange: (Int, String) -> Unit,
    onAddPolicy: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "นโยบาย",
            style = MaterialTheme.typography.titleMedium
        )

        policies.forEachIndexed { index, policy ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF2F2F2)
                ),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    // หมายเลข
                    Text(
                        text = "${index + 1}",
                        fontSize = 13.sp,
                        color = Color(0xFF888888),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    // ชื่อนโยบาย — underline style ไม่มี border
                    TextField(
                        value = policy.title,
                        onValueChange = { onTitleChange(index, it) },
                        label = { Text("ชื่อนโยบาย", fontSize = 13.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        supportingText = { Text("${policy.title.length}/100") },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color(0xFF888888),
                            unfocusedIndicatorColor = Color(0xFFCCCCCC),
                        )
                    )

                    // คำอธิบาย — underline style ไม่มี border
                    TextField(
                        value = policy.detail,
                        onValueChange = { onDetailChange(index, it) },
                        label = { Text("คำอธิบาย", fontSize = 13.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp),
                        supportingText = { Text("${policy.detail.length}/1000") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color(0xFF888888),
                            unfocusedIndicatorColor = Color(0xFFCCCCCC),
                        )
                    )
                }
            }
        }

        // ปุ่มเพิ่มนโยบาย
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF2F2F2)
            ),
            elevation = CardDefaults.cardElevation(0.dp),
            onClick = onAddPolicy
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color(0xFF9E9E9E),
                    modifier = Modifier.size(28.dp)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "เพิ่มนโยบาย",
                    fontSize = 13.sp,
                    color = Color(0xFF9E9E9E)
                )
            }
        }
    }
}