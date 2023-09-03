package com.example.kscache.stream

import com.example.dto.CacheItemMessage
import com.example.dto.CacheItemRecord
import org.apache.kafka.streams.kstream.Branched
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.KTable
import org.apache.kafka.streams.kstream.Named
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Consumer

@Configuration
class CacheItemProcessor {
    @Bean
    fun cacheItem() = Consumer<KStream<String, CacheItemMessage>> { input ->
        val cache: KTable<String, CacheItemRecord> = input.peek { key, value ->
            println("cache process start")
        }.groupByKey()
            .aggregate(
                {
                    println("creating a new cache record.")
                    CacheItemRecord(false, 0)
                },
                { key, message, prevRecord ->
                    when (message.command) {
                        "load" -> {
                            if (prevRecord.isLoaded) {
                                // 이전 캐시가 로딩이 되었다면 처리를 하지 않는다.
                                println("load command rejected. write to database. key=${key}, amount=${prevRecord.amount}")
                                prevRecord
                            } else {
                                // 이전 캐시가 로딩되지 않았다면 캐시를 새로 만든다
                                println("cache loaded. key=${key}, amount=${message.amount}")
                                CacheItemRecord(true, message.amount)
                            }
                        }

                        "unload" -> {
                            if (prevRecord.isLoaded) {
                                // 이전 캐시가 로딩이 되었다면 DB에 기록하고, state를 제거한다.
                                println("cache unloaded. write to database. key=${key}, amount=${prevRecord.amount}")
                                null
                            } else {
                                // 이전 캐시가 로딩되지 않았다면 처리를 하지 않는다.
                                println("unload command rejected. cache not loaded. key=${key}, amount=${prevRecord.amount}")
                                prevRecord
                            }
                        }

                        "add" -> {
                            if (prevRecord.isLoaded) {
                                // 이전 캐시가 로딩이 되었다면 캐시를 업데이트 한다.
                                val nextRecord = CacheItemRecord(true, prevRecord.amount + message.amount)
                                println("cache adding. key=${key}, prevAmount=${prevRecord.amount}, nextAmount=${nextRecord.amount}")
                                nextRecord
                            } else {
                                // 이전 캐시가 로딩되지 않았다면 처리를 하지 않는다.
                                println("add command rejected. cache not loaded. key=${key}, amount=${prevRecord.amount}")
                                prevRecord
                            }
                        }

                        else -> {
                            println("unknown command received.")
                            prevRecord
                        }
                    }
                }
            )
    }
}
