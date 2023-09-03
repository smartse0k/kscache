package com.example.kscache.dto

import com.fasterxml.jackson.annotation.JsonAutoDetect

@JsonAutoDetect
data class CacheItemRecord(
    val isLoaded: Boolean = false,
    val amount: Long = 0
)
