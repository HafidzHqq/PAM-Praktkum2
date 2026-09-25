package com.example.pam

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    MaterialTheme {
        val coroutineScope = rememberCoroutineScope()
        val simulator = remember { NewsFeedSimulator(coroutineScope) }
        
        val readCount by simulator.readCount.collectAsState()
        val newsList = remember { mutableStateListOf<DisplayNews>() }
        var selectedCategory by remember { mutableStateOf<String?>(null) }
        var selectedNewsDetail by remember { mutableStateOf<String?>(null) }
        
        LaunchedEffect(selectedCategory) {
            newsList.clear()
            simulator.getNewsFeed(selectedCategory)
                .catch { e -> println("Error loading news: ${e.message}") }
                .collect { news -> newsList.add(0, news) }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("News Feed", fontWeight = FontWeight.Bold) },
                    actions = {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.padding(end = 16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "$readCount Dibaca",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues)
            ) {
                // Categories
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val categories = listOf(null, "Tech", "Sports", "Politics", "Entertainment")
                    items(categories) { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            label = { Text(category ?: "Semua") },
                            shape = RoundedCornerShape(16.dp)
                        )
                    }
                }
                
                // News List
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(newsList, key = { it.id }) { news ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    coroutineScope.launch {
                                        try {
                                            selectedNewsDetail = "Loading..."
                                            simulator.markAsRead(news.id)
                                            val detail = simulator.fetchNewsDetail(news.id)
                                            selectedNewsDetail = detail
                                        } catch (e: Exception) {
                                            selectedNewsDetail = "Gagal memuat: ${e.message}"
                                        }
                                    }
                                }
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = news.category,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = news.displayTitle.replace("[${news.category}] ", ""),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }

            // Dialog for detail
            if (selectedNewsDetail != null) {
                AlertDialog(
                    onDismissRequest = { selectedNewsDetail = null },
                    title = { Text("Detail Berita") },
                    text = { Text(selectedNewsDetail!!) },
                    confirmButton = {
                        TextButton(onClick = { selectedNewsDetail = null }) {
                            Text("Tutup")
                        }
                    }
                )
            }
        }
    }
}