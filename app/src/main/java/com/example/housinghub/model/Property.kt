package com.example.housinghub

import com.google.firebase.Timestamp

data class Property(
    val id: String = "",
    val title: String = "",
    val type: String = "",
    val price: Double = 0.0, // Changed to Double to match Firestore
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val images: List<String> = emptyList(),
    val videos: List<String> = emptyList(),
    val ownerId: String = "",
    val createdAt: Timestamp? = null,
    val isAvailable: Boolean = true
)