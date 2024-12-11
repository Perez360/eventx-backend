package com.codex.business.components.kyc.repo

import dev.morphia.annotations.PostPersist
import org.slf4j.LoggerFactory

class KycEntityListener {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostPersist
    fun postPersist(kyc: Kyc) {
        logger.info("[ KycEntityListener @PostPersist ]: {}", kyc)
    }
}