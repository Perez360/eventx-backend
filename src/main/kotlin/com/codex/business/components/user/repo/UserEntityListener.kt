package com.codex.business.components.user.repo

import dev.morphia.annotations.PostPersist
import org.slf4j.LoggerFactory

class UserEntityListener {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostPersist
    fun postPersist(user: User) {
        logger.info("[ UserEntityListener @PostPersist ]: {}", user)
    }
}