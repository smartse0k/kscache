package com.example.kscache.dto

import com.fasterxml.jackson.annotation.JsonAutoDetect

@JsonAutoDetect
data class CacheItemMessage(
    val command: String = "",
    val amount: Long = 0
)
