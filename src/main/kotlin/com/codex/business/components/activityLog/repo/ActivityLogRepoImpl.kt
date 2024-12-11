package com.codex.business.components.activityLog.repo

import com.codex.base.shared.PagedContent
import com.codex.base.utils.search
import com.codex.business.components.activityLog.dto.ActivityLogDTO
import com.codex.business.components.activityLog.spec.ActivityLogSpec
import dev.morphia.Datastore
import dev.morphia.DeleteOptions
import dev.morphia.query.filters.Filters
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ActivityLogRepoImpl : ActivityLogRepo, KoinComponent {
    private val datastore: Datastore by inject()

    override fun create(log: ActivityLog): ActivityLog = datastore.save(log)

    override fun count(): Long = datastore.find(ActivityLog::class.java).count()

    override fun findById(id: String): ActivityLog? =
        datastore.find(ActivityLog::class.java).filter(Filters.eq("_id", id)).first()

    override fun search(spec: ActivityLogSpec): PagedContent<ActivityLogDTO> =
        datastore.find(ActivityLog::class.java).search(spec)

    override fun list(page: Int, size: Int): PagedContent<ActivityLogDTO> {
        val spec = ActivityLogSpec()
        spec.page = page
        spec.size = size

        return datastore.find(ActivityLog::class.java)
            .search(spec)
    }

    override fun deleteById(id: String): ActivityLog? =
        datastore.find(ActivityLog::class.java).filter(Filters.eq("_id", id)).findAndDelete()

    override fun deleteAll(): Boolean {
        return datastore.find(ActivityLog::class.java)
            .delete(DeleteOptions().multi(true)).wasAcknowledged()
    }
}