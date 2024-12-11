package com.codex.business.components.eventCategory.spec


import com.codex.base.shared.Spec
import com.codex.business.common.enums.Status
import com.codex.business.components.user.repo.User
import dev.morphia.query.filters.Filter
import dev.morphia.query.filters.Filters
import java.time.LocalDateTime

data class EventCategorySpec(
    var name: String? = null,
    var description: String? = null,
    var status: Status? = null,
    var startDate: LocalDateTime? = null,
    var endDate: LocalDateTime? = null,
    var createdAt: LocalDateTime? = null,
    var modifiedAt: LocalDateTime? = null,
) : Spec() {

    override fun toParameters(): Map<String, Any?> = mapOf(
        ::name.name to name,
        ::description.name to description,
        ::status.name to status,
        ::createdAt.name to createdAt,
        ::modifiedAt.name to modifiedAt,
        ::startDate.name to startDate,
        ::endDate.name to endDate
    )


    override fun queryDefinition(param: Map.Entry<String, Any?>): Filter {
        return when (param.key) {
            ::name.name,
            ::description.name -> Filters.regex(param.key, param.value.toString())
                .caseInsensitive()

            ::startDate.name -> Filters.gte(User::createdAt.name, param.value!!)
            ::endDate.name -> Filters.lte(User::createdAt.name, param.value!!)
            else -> Filters.eq(param.key, param.value)
        }
    }

    companion object {
        fun mapFrom(map: Map<String, Any?>): Spec {
            val name: String? by map
            val description: String? by map
            val status: Status? by map
            val createdAt: LocalDateTime? by map
            val modifiedAt: LocalDateTime? by map
            val startDate: LocalDateTime? by map
            val endDate: LocalDateTime? by map

            return EventCategorySpec(
                name,
                description,
                status,
                createdAt,
                modifiedAt,
                startDate,
                endDate
            )
        }
    }

    override fun toString(): String {
        return "EventCategorySpec(" +
                "name=$name, " +
                "description=$description, " +
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
