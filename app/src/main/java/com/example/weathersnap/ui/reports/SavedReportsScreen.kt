package com.example.weathersnap.ui.reports

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SavedReportsScreen(
    viewModel: ReportsListViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val reports by viewModel.reports.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack, modifier = Modifier.padding(end = 8.dp)) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onBackground)
            }
            Text(
                "Saved Reports",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (reports.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No reports saved yet", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(reports) { report ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column {
                            if (File(report.imagePath).exists()) {
                                AsyncImage(
                                    model = File(report.imagePath),
                                    contentDescription = "Report Image",
                                    modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(report.cityName, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f).padding(end = 16.dp))
                                    Text("${report.temperature.toInt()}°C", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                }
                                Text(report.condition, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                val format = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                                Text(format.format(Date(report.timestamp)), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    val formatSize = { kb: Long ->
                                        if (kb >= 1024) String.format(Locale.US, "%.1f MB", kb / 1024f) else "$kb KB"
                                    }
                                    SuggestionChip(
                                        onClick = {},
                                        label = { Text("Orig: ${formatSize(report.originalSizeKb)}", color = MaterialTheme.colorScheme.onSurface) }
                                    )
                                    SuggestionChip(
                                        onClick = {},
                                        label = { Text("Comp: ${formatSize(report.compressedSizeKb)}", color = MaterialTheme.colorScheme.primary) }
                                    )
                                }
                                
                                if (report.notes.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Notes:", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                                    Text(report.notes, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
