package com.codex.business.components.eventCategory.repo

import com.codex.business.components.event.repo.Event
import dev.morphia.annotations.PostPersist
import org.slf4j.LoggerFactory

class EventCategoryEntityListener {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostPersist
    fun postPersist(eventCategory: EventCategory) {
        logger.info("[ EventCategoryEntityListener @PostPersist ]: {}", eventCategory)
    }
}