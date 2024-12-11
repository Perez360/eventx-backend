package com.codex.business.components.comment.spec


import com.codex.base.shared.Spec
import com.codex.business.components.comment.repo.Comment
import dev.morphia.query.filters.Filter
import dev.morphia.query.filters.Filters
import java.time.LocalDateTime

data class CommentSpec(
    var message: String? = null,
    var eventId: String? = null,
    var userId: String? = null,
    var show: Boolean? = null,
    var startDate: LocalDateTime? = null,
    var endDate: LocalDateTime? = null,
    var createdAt: LocalDateTime? = null,
    var modifiedAt: LocalDateTime? = null,
) : Spec() {

    override fun toParameters(): Map<String, Any?> = mapOf(
        ::message.name to message,
        ::eventId.name to eventId,
        ::userId.name to userId,
        ::show.name to show,
        ::createdAt.name to createdAt,
        ::modifiedAt.name to modifiedAt,
        ::startDate.name to startDate,
        ::endDate.name to endDate
    )


    override fun queryDefinition(param: Map.Entry<String, Any?>): Filter {
        return when (param.key) {
            ::message.name -> Filters.regex(param.key, param.value.toString())
                .caseInsensitive()

            ::startDate.name -> Filters.gte(Comment::createdAt.name, param.value.toString())
            ::endDate.name -> Filters.lte(Comment::createdAt.name, param.value!!)
            else -> Filters.eq(param.key, param.value)
        }
    }

    companion object {
        fun mapFrom(map: Map<String, Any?>): Spec {
            val message: String? by map
            val eventId: String? by map
            val userId: String? by map
            val show: Boolean? by map
            val createdAt: LocalDateTime? by map
            val modifiedAt: LocalDateTime? by map
            val startDate: LocalDateTime? by map
            val endDate: LocalDateTime? by map

            return CommentSpec(message, eventId, userId, show, createdAt, modifiedAt, startDate, endDate)
        }
    }

    override fun toString(): String {
        return "CommentSpec(" +
                "message=$message, " +
                "eventId=$eventId, " +
                "userId=$userId, " +
                "show=$show, " +
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