package com.codex.base.core.settings

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import org.koin.core.component.KoinComponent
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.*
import java.util.concurrent.Executors

class CacheManager<T> : KoinComponent {
    private val memory: Cache<String, T> = Caffeine.newBuilder()
        .executor(Executors.newWorkStealingPool())
        .expireAfterAccess(Duration.ofMinutes(24))
        .removalListener<String, T> { key, _, removalCause ->
            logger.info("Record with key [$key] remove due to $removalCause")
        }
        .build()
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun cache(key: String, value: T) {
        memory.put(key, value)
        logger.info("[$key]- Record saved to cache")
    }

    fun findById(key: String): T? {
        val value = memory.getIfPresent(key)
        if (Objects.nonNull(value)) {
            logger.info("Found record in cache: $value")
        } else {
            logger.info("No record found in cache")
        }
        return value
    }

    fun deleteById(key: String) = memory.invalidate(key)

    fun deleteAll() = memory.cleanUp()

}