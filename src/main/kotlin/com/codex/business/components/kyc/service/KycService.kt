package com.codex.business.components.kyc.service

import com.codex.base.shared.AppResponse
import com.codex.base.shared.PagedContent
import com.codex.business.components.kyc.dto.CreateKycDto
import com.codex.business.components.kyc.dto.KycDto
import com.codex.business.components.kyc.dto.UpdateKycDto
import com.codex.business.components.kyc.repo.Kyc
import com.codex.business.components.kyc.spec.KycSpec


interface KycService {
    fun ping(): AppResponse<Unit>

    fun create(dto: CreateKycDto): AppResponse<Kyc>

    fun update(dto: UpdateKycDto): AppResponse<Kyc>

    fun findById(id: String): AppResponse<Kyc>

    fun list(page: Int = 1, size: Int = 50): AppResponse<PagedContent<KycDto>>

    fun search(spec: KycSpec): AppResponse<PagedContent<KycDto>>
}