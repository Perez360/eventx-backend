package com.codex.business.components.userProfile.repo

import com.codex.base.shared.PagedContent
import com.codex.base.utils.search
import com.codex.business.components.userProfile.dto.UserProfileDto
import com.codex.business.components.userProfile.spec.UserProfileSpec
import dev.morphia.Datastore
import dev.morphia.DeleteOptions
import dev.morphia.InsertOneOptions
import dev.morphia.query.filters.Filters
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserProfileRepoImpl : UserProfileRepo, KoinComponent {
    private val datastore: Datastore by inject()

    override fun create(userProfile: UserProfile): UserProfile = datastore.save(userProfile)

    override fun update(userProfile: UserProfile): UserProfile = datastore.merge(userProfile, InsertOneOptions().unsetMissing(false))

    override fun exists(id: String): Boolean =
        datastore.find(UserProfile::class.java).filter(Filters.eq("_id", id)).toList().isNotEmpty()

    override fun count(): Long = datastore.find(UserProfile::class.java).count()

    override fun findById(id: String): UserProfile? = datastore.find(UserProfile::class.java).filter(Filters.eq("_id", id)).first()

    override fun search(spec: UserProfileSpec): PagedContent<UserProfileDto> = datastore.find(UserProfile::class.java).search(spec)

    override fun list(page: Int, size: Int): PagedContent<UserProfileDto> {
        val spec = UserProfileSpec()
        spec.page = page
        spec.size = size

        return datastore.find(UserProfile::class.java)
            .search(spec)
    }

    override fun deleteById(id: String): UserProfile? =
        datastore.find(UserProfile::class.java).filter(Filters.eq("_id", id)).findAndDelete()

    override fun deleteAll(): Boolean {
        return datastore.find(UserProfile::class.java)
            .delete(DeleteOptions().multi(true)).wasAcknowledged()
    }
}