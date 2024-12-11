package com.codex.business.components.eventReaction.repo

import dev.morphia.annotations.PostPersist
import org.slf4j.LoggerFactory

class EventReactionEntityListener {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostPersist
    fun postPersist(eventReaction: EventReaction) {
        logger.info("[ EventReactionEntityListener @PostPersist ]: {}", eventReaction)
    }
}