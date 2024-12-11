package com.codex.business.components.activityLog.repo

import com.codex.base.shared.PagedContent
import com.codex.business.components.activityLog.dto.ActivityLogDTO
import com.codex.business.components.activityLog.spec.ActivityLogSpec

interface ActivityLogRepo {
    fun create(log: ActivityLog): ActivityLog

    fun findById(id: String): ActivityLog?

    fun count(): Long

    fun list(page: Int = 1, size: Int = 50): PagedContent<ActivityLogDTO>

    fun search(spec: ActivityLogSpec): PagedContent<ActivityLogDTO>

    fun deleteById(id: String): ActivityLog?

    fun deleteAll(): Boolean
}