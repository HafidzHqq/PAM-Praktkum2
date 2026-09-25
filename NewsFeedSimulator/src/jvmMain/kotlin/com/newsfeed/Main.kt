package com.newsfeed

import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val manager = NewsFeedManager()
    val targetCategory = "Tech"

    println("=== Memulai News Feed Simulator (KMP JVM Target) ===")
    println("Mencari berita dengan kategori: $targetCategory\n")

    // Observer untuk StateFlow (Jumlah berita dibaca)
    val stateJob = launch {
        manager.readNewsCount.collect { count ->
            if (count > 0) {
                println(">>> [STATUS] Total berita kategori $targetCategory yang sudah dibaca: $count")
                println("---------------------------------------------------")
            }
        }
    }

    // Mengumpulkan dan memproses Flow dari NewsFeedManager
    val flowJob = launch {
        manager.getNewsFlow()
            // 2. Filter berita berdasarkan kategori tertentu
            .filter { it.category == targetCategory }
            
            // 3. Transform data menjadi format yang ditampilkan
            .map { news ->
                "[FORMAT TED] \n Kategori : ${news.category} \n Judul    : ${news.title} \n (ID: ${news.id})"
            }
            
            // Menggunakan onEach untuk melakukan side-effect saat data diterima
            .onEach { formattedNews ->
                println("Menerima berita baru sesuai filter:")
                println(formattedNews)
            }
            // Error handling untuk menangkap exception di Flow
            .catch { e -> 
                println(">>> [ERROR] Terjadi kesalahan pada Flow: ${e.message}") 
            }
            // Collect terminal operator untuk mengeksekusi flow
            .collect { formattedNews ->
                val idString = formattedNews.substringAfter("(ID: ").substringBefore(")")
                val id = idString.toIntOrNull() ?: 1

                println("-> Mengambil detail berita secara asynchronous...")
                
                try {
                    // Mengambil detail berita menggunakan coroutine async
                    val detail = manager.fetchNewsDetailAsync(id)
                    println("-> Detail Berita Diperoleh: ${detail.content}")
                    
                    // Update StateFlow
                    manager.incrementReadCount(id)
                } catch (e: Exception) {
                    println(">>> [ERROR] Gagal mengambil detail: ${e.message}")
                }
            }
    }

    // Biarkan simulator berjalan selama 15 detik, kemudian hentikan
    delay(15000)
    println("\n=== Waktu simulasi selesai (15 detik) ===")
    flowJob.cancelAndJoin()
    stateJob.cancelAndJoin()
    println("Program berakhir.")
}
