package com.codex.business.components.comment.dto

data class UpdateCommentDTO(
    val id: String? = null,
    val message: String? = null,
    val reaction: String? = null,
)