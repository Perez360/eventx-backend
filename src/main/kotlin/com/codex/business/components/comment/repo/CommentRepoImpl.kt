package com.codex.business.components.comment.repo

import com.codex.base.shared.PagedContent
import com.codex.base.utils.search
import com.codex.business.components.comment.dto.CommentDto
import com.codex.business.components.comment.spec.CommentSpec
import dev.morphia.Datastore
import dev.morphia.DeleteOptions
import dev.morphia.InsertOneOptions
import dev.morphia.query.filters.Filters
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CommentRepoImpl : CommentRepo, KoinComponent {
    private val datastore: Datastore by inject()

    override fun create(comment: Comment): Comment = datastore.save(comment)

    override fun update(comment: Comment): Comment = datastore.merge(comment, InsertOneOptions().unsetMissing(false))

    override fun count(): Long = datastore.find(Comment::class.java).count()

    override fun findById(id: String): Comment? = datastore.find(Comment::class.java)
        .filter(Filters.eq("_id", id)).first()

    override fun search(spec: CommentSpec): PagedContent<CommentDto> = datastore.find(Comment::class.java).search(spec)


    override fun list(page: Int, size: Int): PagedContent<CommentDto> {
        val spec = CommentSpec()
        spec.page = page
        spec.size = size


        return datastore.find(Comment::class.java)
            .search(spec)
    }

    override fun deleteById(id: String): Comment? =
        datastore.find(Comment::class.java).filter(Filters.eq("_id", id)).findAndDelete()

    override fun deleteAll(): Boolean = datastore.find(Comment::class.java)
        .delete(DeleteOptions().multi(true)).wasAcknowledged()

}