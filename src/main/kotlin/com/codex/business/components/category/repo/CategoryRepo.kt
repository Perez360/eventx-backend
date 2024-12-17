package com.codex.business.components.category.repo

import com.codex.base.shared.PagedContent
import com.codex.business.components.category.dto.CategoryDto
import com.codex.business.components.category.spec.CategorySpec

interface CategoryRepo {
    fun create(category: Category): Category

    fun update(category: Category): Category

    fun findById(id: String): Category?

    fun exists(id: String): Boolean

    fun count(): Long

    fun list(page: Int = 1, size: Int = 50): PagedContent<CategoryDto>

    fun search(spec: CategorySpec): PagedContent<CategoryDto>

    fun deleteById(id: String): Category?

    fun deleteAll(): Boolean
}