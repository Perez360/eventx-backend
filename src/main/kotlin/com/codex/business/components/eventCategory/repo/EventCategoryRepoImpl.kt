package com.codex.business.components.eventCategory.repo

import com.codex.base.shared.PagedContent
import com.codex.base.utils.search
import com.codex.business.components.eventCategory.dto.EventCategoryDto
import com.codex.business.components.eventCategory.spec.EventCategorySpec
import dev.morphia.Datastore
import dev.morphia.DeleteOptions
import dev.morphia.InsertOneOptions
import dev.morphia.query.filters.Filters
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EventCategoryRepoImpl : EventCategoryRepo, KoinComponent {
    private val datastore: Datastore by inject()

    override fun create(eventCategory: EventCategory): EventCategory = datastore.save(eventCategory)

    override fun update(eventCategory: EventCategory): EventCategory =
        datastore.merge(eventCategory, InsertOneOptions().unsetMissing(false))

    override fun exists(id: String): Boolean =
        datastore.find(EventCategory::class.java).filter(Filters.eq("_id", id)).toList().isNotEmpty()

    override fun count(): Long = datastore.find(EventCategory::class.java).count()

    override fun findById(id: String): EventCategory? =
        datastore.find(EventCategory::class.java).filter(Filters.eq("_id", id)).first()

    override fun search(spec: EventCategorySpec): PagedContent<EventCategoryDto> =
        datastore.find(EventCategory::class.java).search(spec)

    override fun list(page: Int, size: Int): PagedContent<EventCategoryDto> {
        val spec = EventCategorySpec()
        spec.page = page
        spec.size = size


        return datastore.find(EventCategory::class.java)
            .search(spec)
    }

    override fun deleteById(id: String): EventCategory? =
        datastore.find(EventCategory::class.java).filter(Filters.eq("_id", id)).findAndDelete()

    override fun deleteAll(): Boolean {
        return datastore.find(EventCategory::class.java)
            .delete(DeleteOptions().multi(true)).wasAcknowledged()
    }
}