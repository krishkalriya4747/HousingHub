package com.example.housinghub.model

import com.google.firebase.Timestamp

data class Property(
    var id: String = "",
    val title: String = "",
    val type: String = "",
    val price: Double = 0.0,
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val images: List<String> = emptyList(),
    val videos: List<String> = emptyList(),
    var ownerId: String = "",
    val createdAt: Timestamp? = null,
    var isAvailable: Boolean = true,
    var isBookmarked: Boolean = false,
    val description: String = "",
    val agreementUrl: String = "",
    val propertyType: String = "",
    val timestamp: String = "",
    val location: String = ""
)
