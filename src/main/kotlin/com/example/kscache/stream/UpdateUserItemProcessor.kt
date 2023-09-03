package com.example.kscache.stream

import com.example.kscache.dto.CacheItemMessage
import com.example.kscache.dto.CacheUser
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.KTable
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.BiFunction

/**
 *
 */
@Configuration
class UpdateUserItemProcessor {
    @Bean
    fun updateUserItem() = BiFunction<KStream<String, CacheItemMessage>, KTable<String, CacheUser>, KStream<String, String>> { input, table ->
        input.peek { key, value ->
            println("a request for update user item received. key=${key}")
        }.join(table) { message, user ->
            println("joined. userName=${user.name}, userExp=${user.experience}, requestAmount=${message.amount}")
            // 일단 DTO 만들기 지치니까 String으로 조합해둔다
            "${user.name}가 ${message.amount}를 업데이트 함."
        }
    }
}