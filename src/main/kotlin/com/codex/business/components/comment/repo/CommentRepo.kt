package com.codex.business.components.comment.repo

import com.codex.base.shared.PagedContent
import com.codex.business.components.comment.dto.CommentDto
import com.codex.business.components.comment.spec.CommentSpec

interface CommentRepo {
    fun create(comment: Comment): Comment

    fun update(comment: Comment): Comment

    fun findById(id: String): Comment?

    fun count(): Long

    fun list(page: Int = 1, size: Int = 50): PagedContent<CommentDto>

    fun search(spec: CommentSpec): PagedContent<CommentDto>

    fun deleteById(id: String): Comment?

    fun deleteAll(): Boolean
}