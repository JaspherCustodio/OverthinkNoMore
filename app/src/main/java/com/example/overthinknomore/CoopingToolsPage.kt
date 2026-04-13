package com.example.overthinknomore

import android.content.Intent
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CoopingToolsPage() {
    val context = LocalContext.current
    val resources = listOf(
        NavigationModel(
            title = "Words of Affirmation",
            desc = "Write positive notes for yourself",
            icon = "✨",
            action = "AFFIRMATION_ACTIVITY",
            type = MyResourceType.REGULAR,
        ),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF9576EF)) // Same brand purple
            .statusBarsPadding()
    ) {
        Text(
            text = "Cooping Tools",
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
                CoopingToolsCard(
                    item = item,
                    onClick = {
                        if (item.action == "AFFIRMATION_ACTIVITY") {
                            val intent = Intent(context, AffirmationActivity::class.java)
                            context.startActivity(intent)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun CoopingToolsCard(item: NavigationModel, onClick: () -> Unit) {
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