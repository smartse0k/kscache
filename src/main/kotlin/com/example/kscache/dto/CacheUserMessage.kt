package com.example.kscache.dto

import com.fasterxml.jackson.annotation.JsonAutoDetect

@JsonAutoDetect
data class CacheUserMessage(
    val command: String = "",
    val name: String = "",
    val experience: Long = 0,
)