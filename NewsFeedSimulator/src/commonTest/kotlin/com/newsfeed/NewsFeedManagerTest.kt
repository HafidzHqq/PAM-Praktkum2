package com.newsfeed

import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NewsFeedManagerTest {

    @Test
    fun testFlowEmitsNewDataPeriodically() = runTest {
        val manager = NewsFeedManager()
        
        // Ambil 3 data pertama dari flow
        val newsList = manager.getNewsFlow().take(3).toList()
        
        // Verifikasi bahwa 3 data berhasil di-emit
        assertEquals(3, newsList.size)
        // Verifikasi ID berurutan
        assertEquals(1, newsList[0].id)
        assertEquals(2, newsList[1].id)
        assertEquals(3, newsList[2].id)
    }

    @Test
    fun testAsyncDetailFetchReturnsCorrectDetail() = runTest {
        val manager = NewsFeedManager()
        val targetId = 5
        
        // Panggil fungsi async
        val detail = manager.fetchNewsDetailAsync(targetId)
        
        // Verifikasi kembalian
        assertEquals(targetId, detail.id)
        assertTrue(detail.content.contains(targetId.toString()))
    }

    @Test
    fun testStateFlowUpdatesReadCount() = runTest {
        val manager = NewsFeedManager()
        
        // Verifikasi nilai awal
        assertEquals(0, manager.readNewsCount.value)
        
        // Tambah count 2 kali
        manager.incrementReadCount(1)
        manager.incrementReadCount(2)
        
        // Verifikasi nilai akhir
        assertEquals(2, manager.readNewsCount.value)
    }
}
