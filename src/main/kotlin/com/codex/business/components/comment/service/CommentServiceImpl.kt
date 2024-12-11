package com.codex.business.components.comment.service

import com.codex.base.exceptions.ServiceException
import com.codex.base.shared.AppResponse
import com.codex.base.shared.PagedContent
import com.codex.base.utils.Generator
import com.codex.business.components.comment.dto.AddCommentDto
import com.codex.business.components.comment.dto.CommentDto
import com.codex.business.components.comment.dto.UpdateCommentDTO
import com.codex.business.components.comment.repo.Comment
import com.codex.business.components.comment.repo.CommentRepo
import com.codex.business.components.comment.spec.CommentSpec
import com.codex.business.components.event.repo.EventRepo
import com.codex.business.components.user.repo.UserRepo
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory

class CommentServiceImpl : CommentService, KoinComponent {
    private val commentRepo: CommentRepo by inject()
    private val userRepo: UserRepo by inject()
    private val eventRepo: EventRepo by inject()

    private val logger = LoggerFactory.getLogger(this::class.java)


    override fun ping(): AppResponse<Unit> = AppResponse.Success("The service was reachable successfully", Unit)

    override fun create(dto: AddCommentDto): AppResponse<Comment> {
        val oneEvent = eventRepo.findById(dto.eventId!!)
            ?: throw ServiceException("No event found with id: ${dto.eventId}")

        val oneUser = userRepo.findById(dto.userId!!)
            ?: throw ServiceException("No user found with id: ${dto.userId}")

        val comment = Comment()
        comment.message = dto.message
        comment.reaction = dto.reaction
        comment.user = oneUser
        comment.eventId = dto.eventId

        val savedComment = commentRepo.create(comment)
        oneEvent.comments?.add(savedComment)
        eventRepo.update(oneEvent)
        logger.info("Comment created successfully: {}", savedComment)
        return AppResponse.Success("Comment created successfully", savedComment)
    }

    override fun update(dto: UpdateCommentDTO): AppResponse<Comment> {
        val oneComment = commentRepo.findById(dto.id!!)
            ?: throw ServiceException("No comment found with id ${dto.id}")

        oneComment.message = dto.message
        oneComment.reaction = dto.reaction

        val updatedComment = commentRepo.update(oneComment)
        logger.info("Comment updated successfully: {}", updatedComment)
        return AppResponse.Success("Comment updated successfully", updatedComment)
    }

    override fun findById(id: String): AppResponse<Comment> {
        val oneComment = commentRepo.findById(id)
            ?: throw ServiceException("No comment found with id $id")
        logger.info("Found a comment: {}", oneComment)
        return AppResponse.Success("Comment fetched successfully", oneComment)
    }

    override fun list(page: Int, size: Int): AppResponse<PagedContent<CommentDto>> {
        val pagedComments = commentRepo.list(page, size)
        logger.info("Listed comment(s) in pages: {}", pagedComments)
        return AppResponse.Success("Comment(s) fetched successfully", pagedComments)
    }

    override fun search(spec: CommentSpec): AppResponse<PagedContent<CommentDto>> {
        val pagedComments: PagedContent<CommentDto> = commentRepo.search(spec)
        logger.info("Searched comment(s) in pages: {}", pagedComments)
        return AppResponse.Success("Comment(s) fetched successfully", pagedComments)
    }

    override fun deleteById(id: String): AppResponse<Comment> {
        val deletedComment = commentRepo.deleteById(id)
            ?: throw ServiceException("No comment found with id $id")
        logger.info("Successfully deleted comment: {}", deletedComment)
        return AppResponse.Success("Comment deleted successfully", deletedComment)
    }


    override fun deleteAll(): AppResponse<Boolean> {
        val isAllDeleted = commentRepo.deleteAll()
        logger.info("Successfully deleted all comments: {}", isAllDeleted)
        return AppResponse.Success("All comments deleted successfully", isAllDeleted)
    }
}