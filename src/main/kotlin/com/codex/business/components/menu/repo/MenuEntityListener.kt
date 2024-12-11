package com.codex.business.components.menu.repo

import dev.morphia.annotations.PostPersist
import org.slf4j.LoggerFactory

class MenuEntityListener {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostPersist
    fun postPersist(menu: Menu) {
        logger.info("[ MenuEntityListener @PostPersist ]: {}", menu)
    }
}