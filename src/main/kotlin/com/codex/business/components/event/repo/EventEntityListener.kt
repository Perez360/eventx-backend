package com.codex.business.components.event.repo

import dev.morphia.annotations.PostPersist
import org.slf4j.LoggerFactory

class EventEntityListener {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostPersist
    fun postPersist(event: Event) {
        logger.info("[ EventEntityListener @PostPersist ]: {}", event)
    }
}