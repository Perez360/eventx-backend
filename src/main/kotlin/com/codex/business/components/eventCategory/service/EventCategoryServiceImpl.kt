package com.codex.business.components.eventCategory.service

import com.codex.base.exceptions.ServiceException
import com.codex.base.shared.AppResponse
import com.codex.base.shared.PagedContent
import com.codex.business.components.eventCategory.dto.AddEventCategoryDto
import com.codex.business.components.eventCategory.dto.EventCategoryDto
import com.codex.business.components.eventCategory.dto.UpdateEventCategoryDto
import com.codex.business.components.eventCategory.repo.EventCategory
import com.codex.business.components.eventCategory.repo.EventCategoryRepo
import com.codex.business.components.eventCategory.spec.EventCategorySpec
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

class EventCategoryServiceImpl : EventCategoryService, KoinComponent {
    private val repo: EventCategoryRepo by inject()
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun ping(): AppResponse<Unit> = AppResponse.Success("The service was reachable successfully", Unit)

    override fun create(dto: AddEventCategoryDto): AppResponse<EventCategory> {
        val eventCategory = EventCategory()
        eventCategory.name = dto.name
        eventCategory.description = dto.description
        eventCategory.createdAt = LocalDateTime.now()

        val savedEventCategory = repo.create(eventCategory)
        logger.info("Successfully saved event category{}", savedEventCategory)
        return AppResponse.Success("Category created successfully", savedEventCategory)
    }

    override fun update(dto: UpdateEventCategoryDto): AppResponse<EventCategory> {
        val oneEventCategory =
            repo.findById(dto.id!!) ?: throw ServiceException("No event category found with id ${dto.id}")
        oneEventCategory.name = dto.name
        oneEventCategory.description = dto.description
        oneEventCategory.status = dto.status
        oneEventCategory.modifiedAt = LocalDateTime.now()

        val updatedEventCategory = repo.update(oneEventCategory)
        logger.info("Successfully updated event category {}", updatedEventCategory)
        return AppResponse.Success("Category updated successfully", updatedEventCategory)
    }

    override fun findById(id: String): AppResponse<EventCategory> {
        val oneEvent = repo.findById(id)
            ?: throw ServiceException("No event category found with id $id")
        logger.info("Successfully found event category {}", oneEvent)
        return AppResponse.Success("Category fetched successfully", oneEvent)
    }

    override fun list(page: Int, size: Int): AppResponse<PagedContent<EventCategoryDto>> {
        val pagedCategories = repo.list(page, size)
        logger.info("Listed event categories in pages: {}", pagedCategories)
        return AppResponse.Success("Categories fetched successfully", pagedCategories)
    }

    override fun search(spec: EventCategorySpec): AppResponse<PagedContent<EventCategoryDto>> {
        val pagedCategories: PagedContent<EventCategoryDto> = repo.search(spec)
        logger.info("Searched event categories in pages: {}", pagedCategories)
        return AppResponse.Success("Categories fetched successfully", pagedCategories)
    }

    override fun deleteById(id: String): AppResponse<EventCategory> {
        val deletedEvent = repo.deleteById(id)
            ?: throw ServiceException("No event category found with id $id")
        logger.info("Successfully deleted category: {}", deletedEvent)
        return AppResponse.Success("Category deleted successfully", deletedEvent)
    }
}