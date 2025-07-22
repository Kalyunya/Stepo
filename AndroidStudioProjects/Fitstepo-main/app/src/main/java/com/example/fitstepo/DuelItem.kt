package com.example.fitstepo

data class DuelItem(
    val title: String,
    val subtitle: String,
    val score1: String,
    val score2: String,
    val iconResId: Int,
    val userEmail: String, // ← Замість avatar1 передаємо email
    val avatar2: Int
)
