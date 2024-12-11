package com.codex.business.components.activityLog.repo

import dev.morphia.annotations.PostPersist
import org.slf4j.LoggerFactory

class ActivityLogEntityListener {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostPersist
    fun postPersist(activityLog: ActivityLog) {
        logger.info("[ ActivityLogEntityListener @PostPersist ]: {}", activityLog)
    }
}