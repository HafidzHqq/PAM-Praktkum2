package com.newsfeed

import com.newsfeed.model.News
import com.newsfeed.model.NewsDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

class NewsFeedManager {
    // 4. StateFlow untuk menyimpan jumlah berita yang sudah dibaca
    private val _readNewsCount = MutableStateFlow(0)
    val readNewsCount: StateFlow<Int> = _readNewsCount.asStateFlow()

    // 1. Flow yang mensimulasikan data berita baru setiap 2 detik
    fun getNewsFlow(): Flow<News> = flow {
        var id = 1
        val categories = listOf("Tech", "Sports", "Politics", "Entertainment")
        
        while (true) {
            val category = categories.random()
            val news = News(id, "Berita Utama Hari Ini #$id", category)
            emit(news) // Emit data berita
            id++
            delay(2000) // Delay 2 detik
        }
    }

    // 5. Coroutines untuk mengambil detail berita secara async
    suspend fun fetchNewsDetailAsync(newsId: Int): NewsDetail = coroutineScope {
        // Menggunakan async dengan Dispatchers.Default (Mendukung KMP common module)
        val deferredDetail = async(Dispatchers.Default) {
            delay(1000) // Simulasi waktu proses pengambilan
            NewsDetail(newsId, "Ini adalah konten detail dari berita ID $newsId. Penjelasan lebih panjang...")
        }
        
        // Menunggu dan mengembalikan hasil (await)
        deferredDetail.await()
    }

    private val readNewsIds = mutableSetOf<Int>()

    // Fungsi untuk menambah jumlah berita yang dibaca ke StateFlow
    fun incrementReadCount(newsId: Int) {
        if (readNewsIds.add(newsId)) {
            _readNewsCount.value = readNewsIds.size
        }
    }
}
