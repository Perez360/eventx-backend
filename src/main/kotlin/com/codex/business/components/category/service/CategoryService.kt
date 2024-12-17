package com.codex.business.components.category.service

import com.codex.base.shared.AppResponse
import com.codex.base.shared.PagedContent
import com.codex.business.components.category.dto.AddCategoryDto
import com.codex.business.components.category.dto.CategoryDto
import com.codex.business.components.category.dto.UpdateCategoryDto
import com.codex.business.components.category.repo.Category
import com.codex.business.components.category.spec.CategorySpec

interface CategoryService {
    fun ping(): AppResponse<Unit>

    fun create(dto: AddCategoryDto): AppResponse<Category>

    fun update(dto: UpdateCategoryDto): AppResponse<Category>

    fun findById(id: String): AppResponse<Category>

    fun list(page: Int = 1, size: Int = 50): AppResponse<PagedContent<CategoryDto>>

    fun search(spec: CategorySpec): AppResponse<PagedContent<CategoryDto>>

    fun deleteById(id: String): AppResponse<Category>
}