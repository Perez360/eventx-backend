package com.codex.business.components.comment.service

import com.codex.base.shared.AppResponse
import com.codex.base.shared.PagedContent
import com.codex.business.components.comment.dto.AddCommentDto
import com.codex.business.components.comment.dto.CommentDto
import com.codex.business.components.comment.dto.UpdateCommentDTO
import com.codex.business.components.comment.repo.Comment
import com.codex.business.components.comment.spec.CommentSpec

interface CommentService {
    fun ping(): AppResponse<Unit>

    fun create(dto: AddCommentDto): AppResponse<Comment>

    fun update(dto: UpdateCommentDTO): AppResponse<Comment>

    fun findById(id: String): AppResponse<Comment>

    fun list(page: Int = 1, size: Int = 50): AppResponse<PagedContent<CommentDto>>

    fun search(spec: CommentSpec): AppResponse<PagedContent<CommentDto>>

    fun deleteById(id: String): AppResponse<Comment>

    fun deleteAll(): AppResponse<Boolean>
}