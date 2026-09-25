
package com.example.pam

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

data class News(
    val id: Int,
    val title: String,
    val category: String,
    val content: String = ""
)

data class DisplayNews(
    val id: Int,
    val displayTitle: String,
    val category: String
)

class NewsFeedSimulator(private val coroutineScope: CoroutineScope) {

    private val _readCount = MutableStateFlow(0)
    val readCount: StateFlow<Int> = _readCount.asStateFlow()

    private val categories = listOf("Tech", "Sports", "Politics", "Entertainment")

    private val rawNewsFlow: SharedFlow<News> = flow {
        var idCounter = 1
        while (true) {
            delay(2000)
            val category = categories.random()
            val news = News(idCounter, "Berita Terkini $idCounter", category)
            emit(news)
            idCounter++
        }
    }.shareIn(
        scope = coroutineScope,
        started = SharingStarted.Eagerly,
        replay = 100
    )

    fun getNewsFeed(filterCategory: String? = null): Flow<DisplayNews> {
        return rawNewsFlow
            .filter { news ->
                filterCategory == null || news.category == filterCategory
            }
            .map { news ->
                DisplayNews(
                    id = news.id,
                    displayTitle = "[${news.category}] ${news.title.uppercase()}",
                    category = news.category
                )
            }
    }

    private val readNewsIds = mutableSetOf<Int>()

    fun markAsRead(newsId: Int) {
        if (readNewsIds.add(newsId)) {
            _readCount.value = readNewsIds.size
        }
    }

    suspend fun fetchNewsDetail(newsId: Int): String {
        return withContext(Dispatchers.Default) {
            delay(1000)
            "detail berita untuk ID $newsId ( diambil secara asynchronous menggunakan Coroutines )"
        }
    }
}
