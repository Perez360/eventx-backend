package com.codex.business.components.eventReaction.spec


import com.codex.base.shared.Spec
import com.codex.business.components.eventReaction.enum.ReactionType
import com.codex.business.components.user.repo.User
import dev.morphia.query.filters.Filter
import dev.morphia.query.filters.Filters
import java.time.LocalDateTime

data class EventReactionSpec(
    var userId: String? = null,
    var eventId: String? = null,
    var type: ReactionType? = null,
    var startDate: LocalDateTime? = null,
    var endDate: LocalDateTime? = null,
    var createdAt: LocalDateTime? = null,
    var modifiedAt: LocalDateTime? = null,
) : Spec() {

    override fun toParameters(): Map<String, Any?> = mapOf(
        ::userId.name to userId,
        ::eventId.name to eventId,
        ::type.name to type,
        ::createdAt.name to createdAt,
        ::modifiedAt.name to modifiedAt,
        ::startDate.name to startDate,
        ::endDate.name to endDate
    )


    override fun queryDefinition(param: Map.Entry<String, Any?>): Filter {
        return when (param.key) {
            ::userId.name,
            ::eventId.name -> Filters.regex(param.key, param.value.toString())
                .caseInsensitive()

            ::startDate.name -> Filters.gte(User::createdAt.name, param.value!!)
            ::endDate.name -> Filters.lte(User::createdAt.name, param.value!!)
            else -> Filters.eq(param.key, param.value)
        }
    }

    companion object {
        fun mapFrom(map: Map<String, Any?>): Spec {
            val userId: String? by map
            val eventId: String? by map
            val type: ReactionType? by map
            val createdAt: LocalDateTime? by map
            val modifiedAt: LocalDateTime? by map
            val startDate: LocalDateTime? by map
            val endDate: LocalDateTime? by map

            return EventReactionSpec(
                userId,
                eventId,
                type,
                createdAt,
                modifiedAt,
                startDate,
                endDate
            )
        }
    }

    override fun toString(): String {
        return "EventReactionSpec(" +
                "userId=$userId, " +
                "eventId=$eventId, " +
                "type=$type, " +
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
