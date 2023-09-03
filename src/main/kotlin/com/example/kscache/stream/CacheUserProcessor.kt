package com.example.kscache.stream

import com.example.kscache.dto.CacheUser
import com.example.kscache.dto.CacheUserMessage
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.KTable
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Function

/**
 * 유저 정보를 KTable로 캐싱합니다.
 */
@Configuration
class CacheUserProcessor {
    @Bean
    fun cacheUser() = Function<KStream<String, CacheUserMessage>, KTable<String, CacheUser?>> { input ->
        input.mapValues { key, message ->
            when (message.command) {
                "load" -> {
                    println("user cached. key=${key}, name=${message.name}, exp=${message.experience}")
                    CacheUser(
                        message.name,
                        message.experience
                    )
                }
                "unload" -> {
                    println("user cache destroyed. key=${key}")
                    null
                }
                else -> {
                    println("unknown command received. cache will be destroyed. key=${key}")
                    throw RuntimeException("unknown command received. cache will be destroyed. key=${key}")
                }
            }
        }.toTable()
    }
}