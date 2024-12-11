package com.codex.base.shared

import com.codex.base.enums.Operator
import com.codex.base.enums.SortOrder
import dev.morphia.query.filters.Filter

abstract class Spec {
    var page: Int = 1
    var size: Int = 50
    var sortBy: String? = "id"
    var sortOrder: SortOrder? = SortOrder.DESC
    var operator: Operator? = Operator.AND

    abstract fun toParameters(): Map<String, Any?>


    abstract fun queryDefinition(param: Map.Entry<String, Any?>): Filter


    override fun toString(): String {
        return "Spec(" +
                "page=$page, " +
                "size=$size, " +
                "sortBy=$sortBy, " +
                "sortOrder=$sortOrder, " +
                "operator=$operator" +
                ")"
    }
}
