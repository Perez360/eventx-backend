package com.codex.business.components.activityLog.spec


import com.codex.base.shared.Spec
import com.codex.business.components.activityLog.repo.ActivityLog
import dev.morphia.query.filters.Filter
import dev.morphia.query.filters.Filters
import java.time.LocalDateTime

data class ActivityLogSpec(
    var description: String? = null,
    var type: String? = null,
    var startDate: LocalDateTime? = null,
    var endDate: LocalDateTime? = null,
    var createdAt: LocalDateTime? = null,
    var modifiedAt: LocalDateTime? = null,
) : Spec() {

    override fun toParameters(): Map<String, Any?> = mapOf(
        ::description.name to description,
        ::type.name to type,
        ::createdAt.name to createdAt,
        ::modifiedAt.name to modifiedAt,
        ::startDate.name to startDate,
        ::endDate.name to endDate
    )


    override fun queryDefinition(param: Map.Entry<String, Any?>): Filter {
        return when (param.key) {
            ::description.name, ::type.name -> Filters.regex(param.key, param.value.toString())
                .caseInsensitive()

            ::startDate.name -> Filters.gte(ActivityLog::createdAt.name, param.value.toString())
            ::endDate.name -> Filters.lte(ActivityLog::createdAt.name, param.value!!)
            else -> Filters.eq(param.key, param.value)
        }
    }

    companion object {
        fun mapFrom(map: Map<String, Any?>): Spec {
            val description: String? by map
            val type: String? by map
            val createdAt: LocalDateTime? by map
            val modifiedAt: LocalDateTime? by map
            val startDate: LocalDateTime? by map
            val endDate: LocalDateTime? by map

            return ActivityLogSpec(description, type, createdAt, modifiedAt, startDate, endDate)
        }
    }

    override fun toString(): String {
        return "ActivityLogSpec(" +
                "description=$description, " +
                "type=$type, " +
                "startDate=$startDate, " +
                "endDate=$endDate, " +
                "createdAt=$createdAt, " +
                "modifiedAt=$modifiedAt" +

                "sortOrder=$sortOrder, " +
                "sortBy=$sortBy, " +
                "operator=$operator, " +
                "page=$page, " +
                "size=$size" +
                ")"
    }


}