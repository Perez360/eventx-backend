package com.codex.business.components.event.service

import com.codex.base.exceptions.ServiceException
import com.codex.base.shared.AppResponse
import com.codex.base.shared.PagedContent
import com.codex.base.utils.Mapper
import com.codex.business.components.event.dto.CreateEventDto
import com.codex.business.components.event.dto.EventDto
import com.codex.business.components.event.dto.UpdateEventDto
import com.codex.business.components.event.enums.EventStatus
import com.codex.business.components.event.repo.Event
import com.codex.business.components.event.repo.EventRepo
import com.codex.business.components.event.spec.EventSpec
import com.codex.business.components.eventCategory.repo.EventCategory
import com.codex.business.components.eventCategory.repo.EventCategoryRepo
import com.codex.business.components.eventTag.repo.EventTag
import com.codex.business.components.eventTag.repo.EventTagRepo
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

class EventServiceImpl : EventService, KoinComponent {
    private val tagRepo: EventTagRepo by inject()
    private val categoryRepo: EventCategoryRepo by inject()
    private val eventRepo: EventRepo by inject()
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun ping(): AppResponse<Unit> = AppResponse.Success("The service was reachable successfully", Unit)

    override fun create(dto: CreateEventDto): AppResponse<EventDto> {
        val currentDateTime = LocalDateTime.now()

        if (dto.startDate!!.isAfter(dto.endDate)) {
            throw ServiceException("Invalid event duration")
        }

        val event = Event()
        event.title = dto.title
        event.description = dto.description
        event.imageLinks = dto.imageLinks
        event.reference = dto.reference
        event.startDate = dto.startDate
        event.endDate = dto.endDate
        event.venue = dto.venue
        event.isPublic = dto.isPublic
        event.tags = dto.tags?.map(::fetchTags)?.toMutableSet()
        event.categories = dto.categories?.map(::fetchCategories)?.toMutableSet()
        event.status = when {
            dto.endDate!!.isBefore(currentDateTime) -> EventStatus.ENDED
            dto.startDate!!.isAfter(currentDateTime) -> EventStatus.UPCOMING
            else -> EventStatus.ONGOING
        }

        val savedEvent = eventRepo.create(event)
        logger.info("Event created successfully: {}", savedEvent)
        return AppResponse.Success("Event created successfully", Mapper.convert(savedEvent))
    }

    override fun update(dto: UpdateEventDto): AppResponse<EventDto> {
        val currentDateTime = LocalDateTime.now()

        if (dto.startDate!!.isAfter(dto.endDate)) {
            throw ServiceException("Invalid event duration")
        }

        val event = eventRepo.findById(dto.id!!) ?: throw ServiceException("No event found with id ${dto.id}")
        event.venue = dto.venue
        event.title = dto.title
        event.description = dto.description
        event.venue = dto.venue
        event.isPublic = dto.isPublic
        event.status = dto.status
        event.modifiedAt = LocalDateTime.now()

        event.status = when {
            dto.endDate!!.isBefore(currentDateTime) -> EventStatus.ENDED
            dto.startDate!!.isAfter(currentDateTime) -> EventStatus.UPCOMING
            else -> EventStatus.ONGOING
        }
        event.tags = dto.tags?.map(::fetchTags)?.toMutableSet()
        event.categories = dto.categories?.map(::fetchCategories)?.toMutableSet()

        val updatedEvent = eventRepo.create(event)
        logger.info("Event updated successfully: {}", updatedEvent)
        return AppResponse.Success("Event updated successfully", Mapper.convert(updatedEvent))
    }

    override fun findById(id: String): AppResponse<EventDto> {
        val event = eventRepo.findById(id)
            ?: throw ServiceException("No event found with id $id")
        logger.info("Found an event: {}", event)
        return AppResponse.Success("Event fetched successfully", Mapper.convert(event))
    }

    override fun list(page: Int, size: Int): AppResponse<PagedContent<EventDto>> {
        val pagedEvents = eventRepo.list(page, size)
        logger.info("Listed event(s) in pages: {}", pagedEvents)
        return AppResponse.Success("Event(s) fetched successfully", pagedEvents)
    }

    override fun search(spec: EventSpec): AppResponse<PagedContent<EventDto>> {
        val pagedEvents = eventRepo.search(spec)
        logger.info("Searched event(s) in pages: {}", pagedEvents)
        return AppResponse.Success("Event(s) fetched successfully", pagedEvents)
    }

    override fun deleteById(id: String): AppResponse<EventDto> {
        val deletedEvent = eventRepo.deleteById(id)
            ?: throw ServiceException("No event found with id $id")
        logger.info("Successfully deleted event: {}", deletedEvent)
        return AppResponse.Success("Event deleted successfully", Mapper.convert(deletedEvent))
    }

    private fun fetchCategories(categoryId: String): EventCategory =
        categoryRepo.findById(categoryId) ?: throw ServiceException("No event category found with id: $categoryId")

    private fun fetchTags(tagId: String): EventTag =
        tagRepo.findById(tagId) ?: throw ServiceException("No event tag found with id: $tagId")
}