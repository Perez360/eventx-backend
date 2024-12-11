package com.codex.business.components.eventTag.repo

import com.codex.base.shared.PagedContent
import com.codex.base.utils.search
import com.codex.business.components.eventTag.dto.EventTagDto
import com.codex.business.components.eventTag.spec.EventTagSpec
import dev.morphia.Datastore
import dev.morphia.DeleteOptions
import dev.morphia.InsertOneOptions
import dev.morphia.query.filters.Filters
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EventTagRepoImpl : EventTagRepo, KoinComponent {
    private val datastore: Datastore by inject()

    override fun create(eventTag: EventTag): EventTag = datastore.save(eventTag)

    override fun update(eventTag: EventTag): EventTag =
        datastore.merge(eventTag, InsertOneOptions().unsetMissing(false))

    override fun exists(id: String): Boolean =
        datastore.find(EventTag::class.java).filter(Filters.eq("_id", id)).toList().isNotEmpty()

    override fun count(): Long = datastore.find(EventTag::class.java).count()

    override fun findById(id: String): EventTag? =
        datastore.find(EventTag::class.java).filter(Filters.eq("_id", id)).first()

    override fun search(spec: EventTagSpec): PagedContent<EventTagDto> =
        datastore.find(EventTag::class.java).search(spec)

    override fun list(page: Int, size: Int): PagedContent<EventTagDto> {
        val spec = EventTagSpec()
        spec.page = page
        spec.size = size

        return datastore.find(EventTag::class.java)
            .search(spec)
    }

    override fun deleteById(id: String): EventTag? =
        datastore.find(EventTag::class.java).filter(Filters.eq("_id", id)).findAndDelete()

    override fun deleteAll(): Boolean {
        return datastore.find(EventTag::class.java)
            .delete(DeleteOptions().multi(true)).wasAcknowledged()
    }
}