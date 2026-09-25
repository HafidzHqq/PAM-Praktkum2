package com.newsfeed.model

data class News(
    val id: Int,
    val title: String,
    val category: String
)

data class NewsDetail(
    val id: Int,
    val content: String
)
