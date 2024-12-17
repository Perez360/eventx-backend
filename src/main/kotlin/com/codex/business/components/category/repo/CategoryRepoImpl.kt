package com.codex.business.components.category.repo

import com.codex.base.shared.PagedContent
import com.codex.base.utils.search
import com.codex.business.components.category.dto.CategoryDto
import com.codex.business.components.category.spec.CategorySpec
import dev.morphia.Datastore
import dev.morphia.DeleteOptions
import dev.morphia.InsertOneOptions
import dev.morphia.query.filters.Filters
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CategoryRepoImpl : CategoryRepo, KoinComponent {
    private val datastore: Datastore by inject()

    override fun create(category: Category): Category = datastore.save(category)

    override fun update(category: Category): Category =
        datastore.merge(category, InsertOneOptions().unsetMissing(false))

    override fun exists(id: String): Boolean =
        datastore.find(Category::class.java).filter(Filters.eq("_id", id)).toList().isNotEmpty()

    override fun count(): Long = datastore.find(Category::class.java).count()

    override fun findById(id: String): Category? =
        datastore.find(Category::class.java).filter(Filters.eq("_id", id)).first()

    override fun search(spec: CategorySpec): PagedContent<CategoryDto> =
        datastore.find(Category::class.java).search(spec)

    override fun list(page: Int, size: Int): PagedContent<CategoryDto> {
        val spec = CategorySpec()
        spec.page = page
        spec.size = size


        return datastore.find(Category::class.java)
            .search(spec)
    }

    override fun deleteById(id: String): Category? =
        datastore.find(Category::class.java).filter(Filters.eq("_id", id)).findAndDelete()

    override fun deleteAll(): Boolean {
        return datastore.find(Category::class.java)
            .delete(DeleteOptions().multi(true)).wasAcknowledged()
    }
}