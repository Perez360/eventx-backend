package com.codex.business.components.eventReaction.repo

import com.codex.base.shared.PagedContent
import com.codex.base.utils.search
import com.codex.business.components.eventReaction.dto.EventReactionDto
import com.codex.business.components.eventReaction.spec.EventReactionSpec
import dev.morphia.Datastore
import dev.morphia.InsertOneOptions
import dev.morphia.query.filters.Filters
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EventReactionRepoImpl : EventReactionRepo, KoinComponent {
    private val datastore: Datastore by inject()

    override fun create(eventReaction: EventReaction): EventReaction = datastore.save(eventReaction)

    override fun update(media: EventReaction): EventReaction =
        datastore.merge(media, InsertOneOptions().unsetMissing(false))

    override fun exists(id: String): Boolean =
        datastore.find(EventReaction::class.java).filter(Filters.eq("_id", id)).toList().isNotEmpty()

    override fun count(): Long = datastore.find(EventReaction::class.java).count()

    override fun findById(id: String): EventReaction? =
        datastore.find(EventReaction::class.java).filter(Filters.eq("_id", id)).first()

    override fun search(spec: EventReactionSpec): PagedContent<EventReactionDto> =
        datastore.find(EventReaction::class.java).search(spec)

    override fun list(page: Int, size: Int): PagedContent<EventReactionDto> {
        val spec = EventReactionSpec()
        spec.page = page
        spec.size = size

        return datastore.find(EventReaction::class.java)
            .search(spec)
    }

    override fun deleteById(id: String): EventReaction? =
        datastore.find(EventReaction::class.java).filter(Filters.eq("_id", id)).findAndDelete()
}