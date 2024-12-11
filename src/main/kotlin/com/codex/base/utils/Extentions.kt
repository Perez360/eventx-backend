package com.codex.base.utils

import com.codex.base.enums.Operator
import com.codex.base.enums.SortOrder
import com.codex.base.shared.AppResponse
import com.codex.base.shared.PagedContent
import com.codex.base.shared.Spec
import dev.morphia.query.FindOptions
import dev.morphia.query.Query
import dev.morphia.query.Sort
import dev.morphia.query.filters.Filters
import io.vertx.core.json.JsonObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*
import java.util.stream.Collectors
import kotlin.math.ceil


inline fun <reified S, reified D> Query<S>.search(spec: Spec): PagedContent<D> {
    val logger: Logger = LoggerFactory.getLogger(Query::class.java)

    logger.info("Starting search with Spec: {}", spec)

    val params = spec.toParameters().filterValues(Objects::nonNull).map(spec::queryDefinition).toTypedArray()

    logger.info("Filtered parameters: {}", params)

    val filter = if (params.isEmpty()) {
        logger.info("No parameters provided. Applying empty filter.")
        filter()
    } else {
        logger.info("Building filter with {} operator and parameters: {}", spec.operator, params)
        filter(if (spec.operator == Operator.OR) Filters.or(*params) else Filters.and(*params))
    }
    val sort = if (spec.sortOrder == SortOrder.DESC) {
        logger.info("Applying descending sort on: {}", spec.sortBy)
        Sort.descending(spec.sortBy!!)
    } else {
        logger.info("Applying ascending sort on: {}", spec.sortBy)
        Sort.ascending(spec.sortBy!!)
    }
    val skip = (spec.page - 1) * spec.size
    val pagedOptions = FindOptions().skip(skip).limit(spec.size).sort(sort)
    logger.info("Executing query with options: {}", pagedOptions)

    val data = filter.iterator(pagedOptions).toList().stream().map<D>(Mapper::convert).collect(Collectors.toList())

    // Correctly calculate total pages
    val totalElements = count()
    val totalPages = ceil(totalElements.toDouble() / spec.size.toDouble()).toInt()

    // Determine hasNextPage and hasPreviousPage
    val hasNextPage = spec.page < totalPages
    val hasPreviousPage = spec.page > 1
    val isFirst = !hasPreviousPage
    val isLast = !hasNextPage


    return PagedContent(
        totalElements = totalElements,
        totalPages = totalPages,
        page = spec.page,
        size = spec.size,
        hasNextPage = hasNextPage,
        hasPreviousPage = hasPreviousPage,
        isFirst = isFirst,
        isLast = isLast,
        data = data
    ).also { logger.info("PagedContent result created: {}", it) }
}

fun <T> AppResponse<T>.toJson(): JsonObject {
    return JsonObject.mapFrom(this)
}