package com.codex.base.shared

data class PagedContent<T>(
    var totalElements: Long = 0,
    var totalPages: Int = 0,
    var page: Int = 1,
    var size: Int = 0,
    var hasNextPage: Boolean = false,
    var hasPreviousPage: Boolean = false,
    var isFirst: Boolean = false,
    var isLast: Boolean = false,
    var data: List<T> = emptyList()
)