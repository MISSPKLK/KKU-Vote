package com.example.project_election.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlin.text.isNotEmpty

@Composable
fun PartyResultCard(
    name: String,
    votes: Int,
    percent: Int,
    image_poster: String
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            // LEFT CONTENT
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {

                // PARTY NAME BUTTON
                Box(
                    modifier = Modifier
                        .background(
                            color = Color(0xFFE7772E),
                            shape = RoundedCornerShape(50)
                        )
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = name,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "คะแนนเสียง",
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row {

                    // VOTES BOX
                    Box(
                        modifier = Modifier
                            .background(
                                Color(0xFFF0F0F0),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(10.dp)
                    ) {
                        Text("$votes คะแนน")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // PERCENT BOX
                    Box(
                        modifier = Modifier
                            .background(
                                Color(0xFFF0F0F0),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(10.dp)
                    ) {
                        Text("$percent %")
                    }

                }

            }

            Spacer(modifier = Modifier.width(10.dp))

            // PARTY IMAGE
            AsyncImage(
                model = if(image_poster.isNotEmpty())
                    "http://10.0.2.2:3000/uploads/$image_poster"
                else
                    null,
                contentDescription = name,
                modifier = Modifier
                    .width(110.dp)
                    .height(150.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

        }

    }

}