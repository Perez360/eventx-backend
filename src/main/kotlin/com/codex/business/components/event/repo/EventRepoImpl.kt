package com.codex.business.components.event.repo

import com.codex.base.shared.PagedContent
import com.codex.base.utils.search
import com.codex.business.components.event.dto.EventDto
import com.codex.business.components.event.spec.EventSpec
import dev.morphia.Datastore
import dev.morphia.DeleteOptions
import dev.morphia.InsertOneOptions
import dev.morphia.query.filters.Filters
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EventRepoImpl : EventRepo, KoinComponent {
    private val datastore: Datastore by inject()

    override fun create(event: Event): Event = datastore.save(event)

    override fun update(event: Event): Event = datastore.merge(event, InsertOneOptions().unsetMissing(false))

    override fun exists(id: String): Boolean =
        datastore.find(Event::class.java).filter(Filters.eq("_id", id)).first() != null

    override fun count(): Long = datastore.find(Event::class.java).count()

    override fun findById(id: String): Event? = datastore.find(Event::class.java).filter(Filters.eq("_id", id)).first()

    override fun search(spec: EventSpec): PagedContent<EventDto> = datastore.find(Event::class.java).search(spec)

    override fun list(page: Int, size: Int): PagedContent<EventDto> {
        val spec = EventSpec()
        spec.page = page
        spec.size = size

        return datastore.find(Event::class.java)
            .search(spec)
    }

    override fun deleteById(id: String): Event? =
        datastore.find(Event::class.java).filter(Filters.eq("_id", id)).findAndDelete()

    override fun deleteAll(): Boolean {
        return datastore.find(Event::class.java)
            .delete(DeleteOptions().multi(true)).wasAcknowledged()
    }
}