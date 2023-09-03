package com.example.kscache.dto

import com.fasterxml.jackson.annotation.JsonAutoDetect

@JsonAutoDetect
data class CacheUser(
    val name: String = "",
    val experience: Long = 0
)