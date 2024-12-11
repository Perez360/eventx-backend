package com.codex.business.components.eventTag.repo

import dev.morphia.annotations.PostPersist
import org.slf4j.LoggerFactory

class EventTagEntityListener {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostPersist
    fun postPersist(eventTag: EventTag) {
        logger.info("[ EventTagEntityListener @PostPersist ]: {}", eventTag)
    }
}