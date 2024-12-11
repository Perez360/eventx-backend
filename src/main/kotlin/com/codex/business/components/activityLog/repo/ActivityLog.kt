package com.codex.business.components.activityLog.repo

import dev.morphia.annotations.Entity
import dev.morphia.annotations.EntityListeners
import dev.morphia.annotations.Id
import dev.morphia.annotations.Version
import java.time.LocalDateTime

@Entity("activity_logs")
@EntityListeners(ActivityLogEntityListener::class)
data class ActivityLog(
    @Id
    var id: String? = null,
    var source: String? = null,
    var description: String? = null,
    var data: Map<String, Any>? = null,
    val type: String? = null,
    var createdAt: LocalDateTime? = null,
    var modifiedAt: LocalDateTime? = null,
    @Version
    var version: Long? = null
)