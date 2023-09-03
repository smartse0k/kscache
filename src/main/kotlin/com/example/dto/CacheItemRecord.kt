package com.example.dto

import com.fasterxml.jackson.annotation.JsonAutoDetect

@JsonAutoDetect
data class CacheItemRecord(
    val isLoaded: Boolean,
    val amount: Long = 0
)
