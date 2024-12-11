package com.codex.business.components.comment.dto

data class AddCommentDto(
    var userId: String? = null,
    var eventId: String? = null,
    var message: String? = null,
    val reaction: String? = null
)