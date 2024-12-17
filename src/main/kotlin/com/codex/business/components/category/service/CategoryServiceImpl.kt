package com.codex.business.components.category.service

import com.codex.base.exceptions.ServiceException
import com.codex.base.shared.AppResponse
import com.codex.base.shared.PagedContent
import com.codex.business.components.category.dto.AddCategoryDto
import com.codex.business.components.category.dto.CategoryDto
import com.codex.business.components.category.dto.UpdateCategoryDto
import com.codex.business.components.category.repo.Category
import com.codex.business.components.category.repo.CategoryRepo
import com.codex.business.components.category.spec.CategorySpec
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

class CategoryServiceImpl : CategoryService, KoinComponent {
    private val repo: CategoryRepo by inject()
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun ping(): AppResponse<Unit> = AppResponse.Success("The service was reachable successfully", Unit)

    override fun create(dto: AddCategoryDto): AppResponse<Category> {
        val category = Category()
        category.name = dto.name
        category.description = dto.description
        category.createdAt = LocalDateTime.now()

        val savedEventCategory = repo.create(category)
        logger.info("Successfully saved event category{}", savedEventCategory)
        return AppResponse.Success("Category created successfully", savedEventCategory)
    }

    override fun update(dto: UpdateCategoryDto): AppResponse<Category> {
        val oneCategory =
            repo.findById(dto.id!!) ?: throw ServiceException("No event category found with id ${dto.id}")
        oneCategory.name = dto.name
        oneCategory.description = dto.description
        oneCategory.status = dto.status
        oneCategory.modifiedAt = LocalDateTime.now()

        val updatedCategory = repo.update(oneCategory)
        logger.info("Successfully updated event category {}", updatedCategory)
        return AppResponse.Success("Category updated successfully", updatedCategory)
    }

    override fun findById(id: String): AppResponse<Category> {
        val oneEvent = repo.findById(id)
            ?: throw ServiceException("No event category found with id $id")
        logger.info("Successfully found event category {}", oneEvent)
        return AppResponse.Success("Category fetched successfully", oneEvent)
    }

    override fun list(page: Int, size: Int): AppResponse<PagedContent<CategoryDto>> {
        val pagedCategories = repo.list(page, size)
        logger.info("Listed event categories in pages: {}", pagedCategories)
        return AppResponse.Success("Categories fetched successfully", pagedCategories)
    }

    override fun search(spec: CategorySpec): AppResponse<PagedContent<CategoryDto>> {
        val pagedCategories: PagedContent<CategoryDto> = repo.search(spec)
        logger.info("Searched event categories in pages: {}", pagedCategories)
        return AppResponse.Success("Categories fetched successfully", pagedCategories)
    }

    override fun deleteById(id: String): AppResponse<Category> {
        val deletedEvent = repo.deleteById(id)
            ?: throw ServiceException("No event category found with id $id")
        logger.info("Successfully deleted category: {}", deletedEvent)
        return AppResponse.Success("Category deleted successfully", deletedEvent)
    }
}