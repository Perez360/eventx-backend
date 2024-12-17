package com.codex.business.components.category.repo

import dev.morphia.annotations.PostPersist
import org.slf4j.LoggerFactory

class CategoryEntityListener {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostPersist
    fun postPersist(category: Category) {
        logger.info("[ CategoryEntityListener @PostPersist ]: {}", category)
    }
}