package com.codex.business.components.kyc.repo

import com.codex.base.shared.PagedContent
import com.codex.base.utils.search
import com.codex.business.components.kyc.dto.KycDto
import com.codex.business.components.kyc.spec.KycSpec
import dev.morphia.Datastore
import dev.morphia.InsertOneOptions
import dev.morphia.query.filters.Filters
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class KycRepoImpl : KycRepo, KoinComponent {
    private val datastore: Datastore by inject()

    override fun create(kyc: Kyc): Kyc = datastore.save(kyc)

    override fun update(kyc: Kyc): Kyc = datastore.merge(kyc, InsertOneOptions().unsetMissing(false))

    override fun exists(id: String): Boolean =
        datastore.find(Kyc::class.java).filter(Filters.eq("_id", id)).toList().isNotEmpty()

    override fun count(): Long = datastore.find(Kyc::class.java).count()

    override fun findById(id: String): Kyc? = datastore.find(Kyc::class.java).filter(Filters.eq("_id", id)).first()

    override fun search(spec: KycSpec): PagedContent<KycDto> = datastore.find(Kyc::class.java).search(spec)

    override fun list(page: Int, size: Int): PagedContent<KycDto> {
        val spec = KycSpec()
        spec.page = page
        spec.size = size

        return datastore.find(Kyc::class.java)
            .search(spec)
    }

    override fun deleteById(id: String): Kyc? =
        datastore.find(Kyc::class.java).filter(Filters.eq("_id", id)).findAndDelete()

}