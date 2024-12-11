package com.codex.business.components.kyc.repo

import com.codex.base.shared.PagedContent
import com.codex.business.components.kyc.dto.KycDto
import com.codex.business.components.kyc.spec.KycSpec

interface KycRepo {
    fun create(kyc: Kyc): Kyc

    fun update(kyc: Kyc): Kyc

    fun findById(id: String): Kyc?

    fun exists(id: String): Boolean

    fun count(): Long

    fun list(page: Int = 1, size: Int = 50): PagedContent<KycDto>

    fun search(spec: KycSpec): PagedContent<KycDto>

    fun deleteById(id: String): Kyc?
}