package com.codex.business.components.userProfile.repo

import dev.morphia.annotations.PostPersist
import org.slf4j.LoggerFactory

class UserProfileEntityListener {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostPersist
    fun postPersist(userProfile: UserProfile) {
        logger.info("[ UserProfileEntityListener @PostPersist ]: {}", userProfile)
    }
}