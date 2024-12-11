package com.codex.business.components.user.repo

import com.codex.base.shared.PagedContent
import com.codex.base.utils.search
import com.codex.business.components.user.dto.UserDto
import com.codex.business.components.user.spec.UserSpec
import dev.morphia.Datastore
import dev.morphia.DeleteOptions
import dev.morphia.InsertOneOptions
import dev.morphia.query.filters.Filters
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserRepoImpl : UserRepo, KoinComponent {

    private val datastore: Datastore by inject()

    override fun create(user: User): User = datastore.save(user)

    override fun update(user: User): User = datastore.merge(user, InsertOneOptions().unsetMissing(false))

    override fun count(): Long = datastore.find(User::class.java).count()

    override fun findById(id: String): User? = datastore.find(User::class.java).filter(Filters.eq("_id", id)).first()

    override fun search(spec: UserSpec): PagedContent<UserDto> = datastore.find(User::class.java).search(spec)

    override fun list(page: Int, size: Int): PagedContent<UserDto> {
        val spec = UserSpec()
        spec.page = page
        spec.size = size

        return datastore.find(User::class.java)
            .search(spec)
    }

    override fun deleteById(id: String): User? =
        datastore.find(User::class.java).filter(Filters.eq("_id", id)).findAndDelete()

    override fun deleteAll(): Boolean {
        return datastore.find(User::class.java)
            .delete(DeleteOptions().multi(true)).wasAcknowledged()
    }
}