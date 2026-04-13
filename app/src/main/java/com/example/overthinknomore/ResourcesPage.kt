package com.example.overthinknomore

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ResourcesPage() {
    val context = LocalContext.current
    var showAboutDialog by remember { mutableStateOf(false) }
    val resources = listOf(
        NavigationModel(
            title = "Emergency Hotline",
            desc = "Immediate support (0919-057-1553)",
            icon = "📞",
            action = "tel:09190571553",
            type = MyResourceType.EMERGENCY,
            customColor = Color(0xFFFFEBEE)
        ),
        NavigationModel(
            title = "4-7-8 Breathing",
            desc = "Guided relaxation technique",
            icon = "🧘",
            action = "https://www.healthline.com/health/4-7-8-breathing"
        ),
        NavigationModel(
            title = "Box Breathing",
            desc = "Reset your nervous system",
            icon = "📦",
            action = "https://www.webmd.com/balance/what-is-box-breathing",
            type = MyResourceType.REGULAR
        ),
        NavigationModel(
            title = "5-4-3-2-1 Grounding",
            desc = "Engage your senses to stay present",
            icon = "🛡️",
            action = "https://www.medicalnewstoday.com/articles/5-4-3-2-1-grounding-technique",
            type = MyResourceType.REGULAR
        ),
        NavigationModel(
            title = "About Us",
            desc = "Learn about OverthinkNoMore",
            icon = "🌿",
            type = MyResourceType.ABOUT
            // 'action' defaults to "" automatically now
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF9576EF)) // Same brand purple
            .statusBarsPadding()
    ) {
        Text(
            text = "Resources",
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(20.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(Color.White)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(17.dp)
        ) {

            items(resources) { item ->
                ResourceCard(
                    item = item,
                    onClick = {
                        if (item.action.startsWith("tel:")) {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse(item.action))
                            context.startActivity(intent)
                        } else if (item.action.startsWith("http")) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.action))
                            context.startActivity(intent)
                        } else if (item.type == MyResourceType.ABOUT) {
                            showAboutDialog = true
                        }
                    }
                )
            }

            item {
                Text(
                    text = "Disclaimer: OverthinkNoMore is an AI support tool and not a replacement for professional medical advice or emergency services. If you are in immediate danger, please use the Emergency Contacts above.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        if (showAboutDialog) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showAboutDialog = false },
                confirmButton = {
                    androidx.compose.material3.TextButton(onClick = { showAboutDialog = false }) {
                        Text("Close", color = Color(0xFF9576EF))
                    }
                },
                title = { Text("About Us 🌿", fontWeight = FontWeight.Bold, color = Color(0xFF9576EF)) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "\t\t\t\t\tOverthinkNoMore is developed at East Systems Colleges of Rizal (ESCR) 🇵🇭. " +
                                    "\nOur mission is to provide immediate, empathetic support for those moments when your mind feels too loud. \n" +
                                    "\t\t\t\t\tWe believe that everyone deserves a safe space to navigate their thoughts without judgment. " +
                                    "By bridging the gap between innovative technology and mental wellbeing, we help you quiet the noise, " +
                                    "rediscover your inner peace, and move forward—one breath at a time.",
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 20.sp,
                            textAlign = TextAlign.Justify
                        )

                        Divider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 1.dp)

                        Column {
                            Text(
                                text = "Lead System Analyst & Developer",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelLarge,
                                color = Color(0xFF9576EF)
                            )
                            Text(
                                text = "Jaspher DV. Custodio",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Text(
                            text = "Created in collaboration with a dedicated team of 9 group members from the ESCR College.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                },
                shape = RoundedCornerShape(24.dp),
                containerColor = Color.White
            )
        }
    }
}


@Composable
fun ResourceCard(item: NavigationModel, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF3EFFF))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = item.icon, fontSize = 32.sp)

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A3E75)
            )
            Text(
                text = item.desc,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}