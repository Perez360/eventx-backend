package com.codex.business.components.event.spec


import com.codex.base.shared.Spec
import com.codex.business.components.event.enums.EventStatus
import dev.morphia.query.filters.Filter
import dev.morphia.query.filters.Filters
import java.time.LocalDateTime

data class EventSpec(
    var title: String? = null,
    var description: String? = null,
    var venue: String? = null,
    var isPublic: Boolean? = null,
    var status: EventStatus? = null,
    var startDate: LocalDateTime? = null,
    var endDate: LocalDateTime? = null,
    var createdAt: LocalDateTime? = null,
    var modifiedAt: LocalDateTime? = null,
) : Spec() {

    override fun toParameters(): Map<String, Any?> = mapOf(
        ::title.name to title,
        ::description.name to description,
        ::venue.name to venue,
        ::status.name to status,
        ::isPublic.name to isPublic,
        ::createdAt.name to createdAt,
        ::modifiedAt.name to modifiedAt,
        ::startDate.name to startDate,
        ::endDate.name to endDate
    )


    override fun queryDefinition(param: Map.Entry<String, Any?>): Filter {
        return when (param.key) {
            ::title.name,
            ::description.name,
            ::venue.name -> Filters.regex(param.key, param.value.toString())
                .caseInsensitive()

            ::startDate.name -> Filters.gte(::createdAt.name, param.value!!)
            ::endDate.name -> Filters.lte(::createdAt.name, param.value!!)
            else -> Filters.eq(param.key, param.value)
        }
    }

    companion object {
        fun mapFrom(map: Map<String, Any?>): Spec {
            val title: String? by map
            val description: String? by map
            val venue: String? by map
            val isPublic: Boolean? by map
            val status: EventStatus? by map
            val createdAt: LocalDateTime? by map
            val modifiedAt: LocalDateTime? by map
            val startDate: LocalDateTime? by map
            val endDate: LocalDateTime? by map

            return EventSpec(
                title,
                description,
                venue,
                isPublic,
                status,
                createdAt,
                modifiedAt,
                startDate,
                endDate
            )
        }
    }

    override fun toString(): String {
        return "EventSpec(" +
                "title=$title, " +
                "description=$description, " +
                "venue=$venue, " +
                "isPublic=$isPublic, " +
                "status=$status, " +
                "startDate=$startDate, " +
                "endDate=$endDate, " +
                "createdAt=$createdAt, " +
                "modifiedAt=$modifiedAt, " +

                "sortOrder=$sortOrder, " +
                "sortBy=$sortBy, " +
                "operator=$operator, " +
                "page=$page, " +
                "size=$size" +
                ")"
    }
}
