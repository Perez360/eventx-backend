package com.codex.business.components.event.dto

import java.time.LocalDateTime


data class CreateEventDto(
    var title: String? = null,
    var description: String? = null,
    var venue: String? = null,
    var isPublic: Boolean? = true,
    var reference: String? = null,
    var imageLinks: MutableSet<String>? = null,
    var startDate: LocalDateTime? = null,
    var endDate: LocalDateTime? = null,
    var tags: MutableSet<String>? = null,
    var categories: MutableSet<String>? = null,
)