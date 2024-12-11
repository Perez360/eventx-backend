package com.codex.business.components.comment.repo

import dev.morphia.annotations.PostPersist
import org.slf4j.LoggerFactory

class CommentEntityListener {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostPersist
    fun postPersist(comment: Comment) {
        logger.info("[ CommentEntityListener @PostPersist ]: {}", comment)
    }
}